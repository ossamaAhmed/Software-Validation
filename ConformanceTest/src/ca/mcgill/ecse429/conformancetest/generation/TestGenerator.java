package ca.mcgill.ecse429.conformancetest.generation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InfixExpression.Operator;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.SimpleName;
import org.junit.Test;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JVar;

import ca.mcgill.ecse429.conformancetest.statemodel.StateMachine;
import ca.mcgill.ecse429.conformancetest.statemodel.Transition;

public final class TestGenerator {
	// Data
	private final StateMachine mStateMachine;
	private final List<ArrayList<Transition>> mPaths;
	
	private final String mTargetClassName;

	// Code Modeling
	private final JCodeModel mModel;
	
	private JDefinedClass mTestClass;		// Model of the text fixture class.
	private final JClass mTargetClass;		// Model of the class being tested.
	private final JClass mAssertClass;		// Model of the JUnit Assert class.
	
	// Tracking
	private int mTestCount;
	
	// Constructor
	public TestGenerator(StateMachine stateMachine, List<ArrayList<Transition>> paths) {
		if (stateMachine == null) {
			throw new IllegalArgumentException("stateMachine");
		}
		if (paths == null) {
			throw new IllegalArgumentException("paths");
		}

		mStateMachine = stateMachine;
		mPaths = paths;
		
		mTestCount = 0;
		 
		mTargetClassName = String.format(
				"%s.%s",
				mStateMachine.getPackageName(),
				mStateMachine.getClassName().replace(".java", ""));

		mModel = new JCodeModel();
		mTargetClass = mModel.ref(mTargetClassName);
		mAssertClass = mModel.ref(org.junit.Assert.class);
	}

	// Public Methods
	public void generateTests(File targetDirectory) throws JClassAlreadyExistsException, IOException {
		generateTestContainerClass();
		
		for (List<Transition> path : mPaths) {
			generateTest(path);
		}
		
		mModel.build(targetDirectory);
	}

	// Private Methods
	private void generateTestContainerClass() throws JClassAlreadyExistsException {
		String testClassName = String.format("ca.mcgill.ecse429.conformancetest.statemodel.GeneratedTest%s", mTargetClass.name());
		mTestClass = mModel._class(testClassName);
	}
	
	private void generateTest(List<Transition> path) {
		TestGenerationState state = new TestGenerationState();
		
		generateTestContainerMethod(state);
		
		for(Transition transition : path) {
			processTransition(state, transition);
		}
	}
	
	private void generateTestContainerMethod(TestGenerationState state) {
		String testMethodName = String.format("%s%d",  "Test", mTestCount + 1);
		mTestCount++;
		
		JMethod testMethod = mTestClass.method(JMod.PUBLIC, mModel.VOID, testMethodName);
		testMethod.annotate(Test.class);
		
		state.setTestMethod(testMethod);
	}
	
	private void processTransition(TestGenerationState state, Transition transition) {
		ActionVerificationCollection verifications = computeActions(state, transition.getAction());
		
		preProcessActions(state, verifications);
		
		processEvent(state, transition.getEvent());
		assertNewState(state, transition.getTo().getName());
		
		postProcessActions(state, verifications);
	}
	
	// Private Model Builder Methods
	private ActionVerificationCollection computeActions(TestGenerationState state, String actions) {
		// Create an AST parser for the actions in the transition.
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setSource(actions.toCharArray());
		parser.setKind(ASTParser.K_STATEMENTS);
		parser.setBindingsRecovery(false);
		parser.setResolveBindings(false);
		
		// Visit each node in the AST and match all assignments.
		ActionVerificationCollection verificationInfo = new ActionVerificationCollection();
		parser.createAST(null).accept(new ASTVisitor() {
			public boolean visit(Assignment node) {
				switch (determineVerificationType(node)) {
				case NUMERICAL_ASSERTION:
					verificationInfo.getNumericalAssertions().add(createNumericalAssertion(node));
					break;

				case BOOLEAN_ASSERTION:
					verificationInfo.getBooleanAssertions().add(createBooleanAssertion(node));
					break;

				case HISTORICAL_NUMERICAL_ASSERTION:
					verificationInfo.getHistoricalNumericalAssertions().add(createHistoricalNumericalAssertion(node));
					break;
					
				default:
					break;
				}

				return false;
			}
		});
		
		return verificationInfo;
	}
	
	private void processEvent(TestGenerationState state, String event) {
		if(event.equals("@ctor")) {
			JVar targetInstance = state.getTestMethodBody().decl(mTargetClass, "target");
			targetInstance.init(JExpr._new(mTargetClass));
			
			state.setTargetInstance(targetInstance);
		} else {
			JInvocation eventInvocation = state.getTargetInstance().invoke(event);
			state.getTestMethodBody().add(eventInvocation);
		}
	}
	
	private void assertNewState(TestGenerationState state, String newState) {
		JInvocation assertInvocation = state.getTestMethodBody().staticInvoke(mAssertClass, "assertEquals");
		
		assertInvocation.arg(newState);
		assertInvocation.arg(state.getTargetInstance().invoke("getStateName"));
	}
	
	private void preProcessActions(TestGenerationState state, ActionVerificationCollection verifications) {
		for(HistoricalNumericalAssertion assertion : verifications.getHistoricalNumericalAssertions()) {
			preProcessHistoricalNumericalAssertion(state, assertion);
		}
	}
	
	private void postProcessActions(TestGenerationState state, ActionVerificationCollection verifications) {
		for(NumericalAssertion assertion : verifications.getNumericalAssertions()) {
			processNumericalAssertion(state, assertion);
		}
		
		for(BooleanAssertion assertion : verifications.getBooleanAssertions()) {
			processBooleanAssertion(state, assertion);
		}
		
		for(HistoricalNumericalAssertion assertion : verifications.getHistoricalNumericalAssertions()) {
			postProcessHistoricalNumericalAssertion(state, assertion);
		}
	}
	
	private void processNumericalAssertion(TestGenerationState state, NumericalAssertion assertion) {
		String getterMethodName = getGetterForField(assertion.getIdentifier());
		
		JInvocation assertInvocation = state.getTestMethodBody().staticInvoke(mAssertClass, "assertEquals");
		assertInvocation.arg(JExpr.lit(assertion.getValue()));
		assertInvocation.arg(state.getTargetInstance().invoke(getterMethodName));
	}
	
	private void processBooleanAssertion(TestGenerationState state, BooleanAssertion assertion) {
		String getterMethodName = getGetterForField(assertion.getIdentifier());
		
		JInvocation assertInvocation = state.getTestMethodBody().staticInvoke(mAssertClass, "assertEquals");
		assertInvocation.arg(JExpr.lit(assertion.getValue()));
		assertInvocation.arg(state.getTargetInstance().invoke(getterMethodName));
	}
	
	private void preProcessHistoricalNumericalAssertion(TestGenerationState state, HistoricalNumericalAssertion assertion) {
		String getterMethodName = getGetterForField(assertion.getPreIdentifier());
		
		JVar variable = state.getTestMethodBody().decl(mModel.INT, String.format("v%d", state.getVariableCount()));
		variable.init(state.getTargetInstance().invoke(getterMethodName));
		
		state.getAssertionVariableMap().put(assertion, variable);
		state.increaseVariableCount();
	}
	
	private void postProcessHistoricalNumericalAssertion(TestGenerationState state, HistoricalNumericalAssertion assertion) {
		JVar variableBefore = state.getAssertionVariableMap().get(assertion);
		JExpression expectedValueExpression = null;
		
		Operator operator = assertion.getOperator();
		if(operator == Operator.PLUS) {
			expectedValueExpression = variableBefore.plus(JExpr.lit(assertion.getConstant()));
		} else if(operator == Operator.MINUS) {
			expectedValueExpression = variableBefore.minus(JExpr.lit(assertion.getConstant()));
		} else if(operator == Operator.TIMES) {
			expectedValueExpression = variableBefore.mul(JExpr.lit(assertion.getConstant()));
		} else if(operator == Operator.DIVIDE) {
			expectedValueExpression = variableBefore.div(JExpr.lit(assertion.getConstant()));
		} else {
			return;
		}
		
		String getterMethodName = getGetterForField(assertion.getPostIdentifier());
		
		JInvocation assertInvocation = state.getTestMethodBody().staticInvoke(mAssertClass, "assertEquals");
		assertInvocation.arg(expectedValueExpression);
		assertInvocation.arg(state.getTargetInstance().invoke(getterMethodName));
	}
	
	// Private AST Methods
	private ActionVerificationType determineVerificationType(Assignment node) {
		// Match the shape of the assignment to either:
		// Assertion:	<variable> = <literal>
		// Historical:	<variable> = <variable> + | - | * | / <literal>
		
		if(isAssignmentNodeNumericalAssertion(node)) {
			return ActionVerificationType.NUMERICAL_ASSERTION;
		} else if(isAssignmentNodeBooleanAssertion(node)) {
			return ActionVerificationType.BOOLEAN_ASSERTION;
		} else if(isAssignmentNodeHistoricalNumericalAssertion(node)) {
			return ActionVerificationType.HISTORICAL_NUMERICAL_ASSERTION;	
		} else {
			return ActionVerificationType.UNKNOWN;
		}
	}
	
	private NumericalAssertion createNumericalAssertion(Assignment node) {
		SimpleName leftSide = (SimpleName) node.getLeftHandSide();
		NumberLiteral rightSide = (NumberLiteral) node.getRightHandSide();
		
		String identifier = leftSide.getIdentifier();
		int literal = Integer.parseInt(rightSide.getToken());
		
		return new NumericalAssertion(identifier, literal);
	}
	
	private BooleanAssertion createBooleanAssertion(Assignment node) {
		SimpleName leftSide = (SimpleName) node.getLeftHandSide();
		BooleanLiteral rightSide = (BooleanLiteral) node.getRightHandSide();
		
		String identifier = leftSide.getIdentifier();
		boolean literal = rightSide.booleanValue();
		
		return new BooleanAssertion(identifier, literal);
	}
	
	private HistoricalNumericalAssertion createHistoricalNumericalAssertion(Assignment node) {
		SimpleName leftSide = (SimpleName) node.getLeftHandSide();
		InfixExpression rightSide = (InfixExpression) node.getRightHandSide();
		
		SimpleName leftInfixSide = (SimpleName) rightSide.getLeftOperand();
		Operator infixOp = rightSide.getOperator(); 
		NumberLiteral rightInfixSide = (NumberLiteral) rightSide.getRightOperand();
		
		String preIdentifier = leftSide.getIdentifier();
		String postIdentifier = leftInfixSide.getIdentifier();
		int constant = Integer.parseInt(rightInfixSide.getToken());
		
		return new HistoricalNumericalAssertion(preIdentifier, postIdentifier, constant, infixOp);
	}
	
	private boolean isAssignmentNodeNumericalAssertion(Assignment node) {
		Expression leftSide = node.getLeftHandSide();
		Expression rightSide = node.getRightHandSide();
		
		// These matches are not exhaustive.
		boolean leftMatch = leftSide.getNodeType() == ASTNode.SIMPLE_NAME; 
		boolean rightMatch = rightSide.getNodeType() == ASTNode.NUMBER_LITERAL;
		
		return leftMatch && rightMatch;
	}
	
	private boolean isAssignmentNodeBooleanAssertion(Assignment node) {
		Expression leftSide = node.getLeftHandSide();
		Expression rightSide = node.getRightHandSide();
		
		// These matches are not exhaustive.
		boolean leftMatch = leftSide.getNodeType() == ASTNode.SIMPLE_NAME; 
		boolean rightMatch = rightSide.getNodeType() == ASTNode.BOOLEAN_LITERAL;
		
		return leftMatch && rightMatch;
	}
	
	private boolean isAssignmentNodeHistoricalNumericalAssertion(Assignment node) {
		Expression leftSide = node.getLeftHandSide();
		Expression rightSide = node.getRightHandSide();
		
		// These matches are not exhaustive.
		boolean leftMatch = leftSide.getNodeType() == ASTNode.SIMPLE_NAME;
		boolean rightMatch = rightSide.getNodeType() == ASTNode.INFIX_EXPRESSION;
		
		return leftMatch && rightMatch;
	}
	
	// Private Test Generation Types
	private class TestGenerationState {
		// Data
		private JMethod mTestMethod;
		private JVar mTargetInstance;
		
		private int mVariableCount;
		private final Map<HistoricalAssertion<?>, JVar> mAssertionVariableMap;
		
		// Constructor
		public TestGenerationState() {
			mVariableCount = 0;
			mAssertionVariableMap = new HashMap<HistoricalAssertion<?>, JVar>();
		}
		
		// Public Methods
		public void increaseVariableCount() {
			mVariableCount++;
		}
		
		// Public Properties
		public void setTestMethod(JMethod testMethod) {
			mTestMethod = testMethod;
		}
		
		public JBlock getTestMethodBody() {
			return mTestMethod.body();
		}
		
		public JVar getTargetInstance() {
			return mTargetInstance;
		}
		public void setTargetInstance(JVar targetInstance) {
			mTargetInstance = targetInstance;
		}
	
		public int getVariableCount() {
			return mVariableCount;
		}
		public Map<HistoricalAssertion<?>, JVar> getAssertionVariableMap() {
			return mAssertionVariableMap;
		}
	}
	
	// Private Action Verification Types
	private enum ActionVerificationType {
		UNKNOWN,
		
		NUMERICAL_ASSERTION,
		BOOLEAN_ASSERTION,
		
		HISTORICAL_NUMERICAL_ASSERTION
	}

	private class ActionVerificationCollection {
		// Data
		private final List<NumericalAssertion> mNumericalAssertions;
		private final List<BooleanAssertion> mBooleanAssertions;
		private final List<HistoricalNumericalAssertion> mHistoricalNumericalAssertions;
		
		// Constructor
		protected ActionVerificationCollection() {
			mNumericalAssertions = new ArrayList<NumericalAssertion>();
			mBooleanAssertions = new ArrayList<BooleanAssertion>();
			mHistoricalNumericalAssertions = new ArrayList<HistoricalNumericalAssertion>();
		}
		
		// Public Properties
		public List<NumericalAssertion> getNumericalAssertions() {
			return mNumericalAssertions;
		}
		public List<BooleanAssertion> getBooleanAssertions() {
			return mBooleanAssertions;
		}

		public List<HistoricalNumericalAssertion> getHistoricalNumericalAssertions() {
			return mHistoricalNumericalAssertions;
		}
	}
	
	private abstract class Assertion<T> {
		// Data
		private String mIdentifier;
		private T mValue;
		
		// Constructor
		protected Assertion(String identifier, T value) {
			if(identifier == null) {
				throw new IllegalArgumentException("identifier");
			}
			
			mIdentifier = identifier;
			mValue = value;
		}
		
		// Public Properties
		public String getIdentifier() {
			return mIdentifier;
		}
		
		public T getValue() {
			return mValue;
		}
	}
	
	private class NumericalAssertion extends Assertion<Integer> {
		// Constructor
		protected NumericalAssertion(String identifier, Integer value) {
			super(identifier, value);
		}
	}
	
	private class BooleanAssertion extends Assertion<Boolean> {
		// Constructor
		protected BooleanAssertion(String identifier, Boolean value) {
			super(identifier, value);
		}
	}

	private abstract class HistoricalAssertion<T> {
		// Data
		private String mPreIdentifier;
		private String mPostIdentifier;
		private T mConstant;
		private Operator mOperator;
		
		// Constructor
		public HistoricalAssertion(
				String preIdentifier,
				String postIdentifier,
				T constant,
				Operator operator) {
			mPreIdentifier = preIdentifier;
			mPostIdentifier = postIdentifier;
			mConstant = constant;
			mOperator = operator;
		}
		
		// Public Properties
		public String getPreIdentifier() {
			return mPreIdentifier;
		}
		public String getPostIdentifier() {
			return mPostIdentifier;
		}
		public T getConstant() {
			return mConstant;
		}
		public Operator getOperator() {
			return mOperator;
		}
	}
	
	private final class HistoricalNumericalAssertion extends HistoricalAssertion<Integer> {
		// Constructor
		public HistoricalNumericalAssertion(
				String preIdentifier,
				String postIdentifier,
				Integer constant,
				Operator operator) {
			super(preIdentifier, postIdentifier, constant, operator);
		}
	}

	// Private Helper Methods
	private String getGetterForField(String fieldName) {
		return String.format(
				"get%c%s",
				Character.toUpperCase(fieldName.charAt(0)),
				fieldName.substring(1));
	}
}
