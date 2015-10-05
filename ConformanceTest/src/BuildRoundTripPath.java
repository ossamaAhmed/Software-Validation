import java.util.ArrayList;
import java.util.Iterator;

import Trees.*;
import ca.mcgill.ecse429.conformancetest.statemodel.StateMachine;
import ca.mcgill.ecse429.conformancetest.statemodel.Transition;
import ca.mcgill.ecse429.conformancetest.statemodel.persistence.PersistenceStateMachine;


public class BuildRoundTripPath {
	
	public static void main (String[] args){
        
		System.out.println("This is my main class");
		PersistenceStateMachine.loadStateMachine("ccoinbox.xml");
		StateMachine x= StateMachine.getInstance();
		Tree tree = new Tree(new Node(x.getTransition(0)));
		tree.buildTree(x);
		ArrayList<ArrayList<Transition>> list= tree.getPaths(x);
		
		for(int i=0;i<list.size();i++){
			System.out.println("Path number :"+(i+1)+" ");
			for(int j=0;j<list.get(i).size();j++){
				System.out.println(list.get(i).get(j).toString());
			}
		}
		System.out.println("Number of paths"+list.size());
	}

}
