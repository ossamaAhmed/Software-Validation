package ca.mcgill.ecse429.conformancetest.generation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.sun.codemodel.JClassAlreadyExistsException;

import ca.mcgill.ecse429.conformancetest.statemodel.StateMachine;
import ca.mcgill.ecse429.conformancetest.statemodel.Transition;
import ca.mcgill.ecse429.conformancetest.statemodel.persistence.PersistenceStateMachine;

public final class TestGeneratorClient {
	public static void main(String[] args) throws JClassAlreadyExistsException, IOException {
		if(args.length != 2) {
			System.out.println("Provide the path to the state machine's XML representation and the resulting package for the test cases.");
			return;
		}
		
		PersistenceStateMachine.loadStateMachine(args[0]);
		StateMachine machine = StateMachine.getInstance();

		Tree tree = new Tree(new Node(machine.getTransition(0)));
		tree.buildTree(machine);

		List<ArrayList<Transition>> paths = tree.getPaths(machine);
		TestGenerator generator = new TestGenerator(machine, paths, args[1]);

		generator.generateTests(new File("src"));
	}
}
