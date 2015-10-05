package ca.mcgill.ecse429.conformancetest.generation;

import java.util.ArrayList;

import ca.mcgill.ecse429.conformancetest.statemodel.StateMachine;
import ca.mcgill.ecse429.conformancetest.statemodel.Transition;

public class Tree {

	private Node root;

	// Constructors
	public Tree() {
		root = null;
	}

	public Tree(Node root) {
		this.root = root;

	}

	public void addRoot(Node root) {
		this.root = root;
	}

	public Node getRoot() {
		return this.root;
	}

	public void buildTree(StateMachine x) {
		for (int i = 1; i < x.getTransitions().size(); i++) {
			insertTransition(this.root, x.getTransition(i), 1);
		}

	}

	public void display(Node x, int counter) {
		ArrayList<Node> children = x.getChildren();

		// System.out.println("Level is :"+counter+"
		// :"+x.getIdentifier().getFrom().getName()+" TO:
		// "+x.getIdentifier().getTo().getName()+"Through
		// event"+x.getIdentifier().getEvent());
		counter++;
		for (Node child : children) {
			// Recursive call
			this.display(child, counter);
		}
	}

	public void insertTransition(Node x, Transition t, int occurences) {
		if (occurences > 1)
			return;

		ArrayList<Node> children = x.getChildren();
		int oldSize = children.size();

		if (t.getFrom().getName() == x.getIdentifier().getTo().getName()) {
			x.addChild(new Node(t));
			occurences++;
		}

		for (int i = 0; i < oldSize; i++) {
			// Recursive call
			this.insertTransition(children.get(i), t, occurences);
		}
	}

	public ArrayList<ArrayList<Transition>> getPaths(StateMachine x) {

		ArrayList<ArrayList<Transition>> list = new ArrayList<ArrayList<Transition>>();
		ArrayList<Transition> firstPath = new ArrayList<Transition>();
		firstPath.add(x.getTransition(0));
		list.add(firstPath);
		getPath(this.root, list, 0);
		return list;

	}

	public void getPath(Node x, ArrayList<ArrayList<Transition>> list, int currentPath) {
		ArrayList<Node> children = x.getChildren();

		if (children.size() >= 1) {
			ArrayList<Transition> myCurrentPath = list.get(currentPath);
			myCurrentPath.add(children.get(0).getIdentifier());
			getPath(children.get(0), list, currentPath);
		}
		for (int i = 1; i < children.size(); i++) {
			// Recursive call
			// create new paths
			ArrayList<Transition> newPath = new ArrayList<Transition>();
			for (int j = 0; j < list.get(currentPath).size() - 1; j++) {
				newPath.add(list.get(currentPath).get(j));
			}
			newPath.add(children.get(i).getIdentifier());
			list.add(newPath);
			getPath(children.get(i), list, list.size() - 1);
		}
	}

}