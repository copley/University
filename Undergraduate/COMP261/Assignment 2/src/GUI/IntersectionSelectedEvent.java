package GUI;
import java.util.EventObject;

import Graph.Node;


public class IntersectionSelectedEvent extends EventObject {
	private Node node;
	
	public IntersectionSelectedEvent(Object source, Node n) {
		super(source);
		node = n;
	}
	
	public Node getNode() {
		return node;
	}
}
