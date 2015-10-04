import ca.mcgill.ecse429.conformancetest.statemodel.StateMachine;
import ca.mcgill.ecse429.conformancetest.statemodel.persistence.PersistenceStateMachine;


public class BuildRoundTripPath {
	
	public static void main (String[] args){
		System.out.println("This is my main class");
		PersistenceStateMachine.loadStateMachine("ccoinbox.xml");
		StateMachine x= StateMachine.getInstance();
		System.out.println(x.numberOfTransitions());
		//print states
//		for(int i=0; i<x.numberOfStates();i++){
//			System.out.println("State Number "+ (i+1) +""+x.getState(i).toString());
//		}
		//create the table for analyzing the roundtrip path
		String [][] roundTrip= new String[5][x.numberOfTransitions()];
		for(int i=0;i<x.numberOfTransitions();i++){
				System.out.print("From:"+x.getTransition(i).getFrom().getName()+ "   ");
				System.out.print("Event: "+ x.getTransition(i).getEvent()+ "  ");
				System.out.print("Condition:"+x.getTransition(i).getCondition()+"  ");
				System.out.print("Action: "+x.getTransition(i).getAction()+"  ");
				System.out.print("ˇTo: "+ x.getTransition(i).getTo().getName()+"  \n");
		}
	}

}
