package ca.mcgill.ecse429.conformancetest.generation;

import java.util.ArrayList;

import ca.mcgill.ecse429.conformancetest.statemodel.Transition;

public class Node {

	private Transition identifier;
	private ArrayList<Node> children;

	// Constructor
	public Node(Transition identifier) {
		this.identifier = identifier;
		children = new ArrayList<Node>();
	}

	// Properties
	public Transition getIdentifier() {
		return identifier;
	}

	public ArrayList<Node> getChildren() {
		return children;
	}

	// Public interface
	public void addChild(Node child) {
		children.add(child);
	}
}