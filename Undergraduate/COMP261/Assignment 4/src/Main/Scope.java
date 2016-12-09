package Main;

import java.util.HashMap;
import java.util.Map;

import ParseTree.ExpressionNodes.ExpressionNode;

public class Scope {
	private Map<String, ExpressionNode> assignments;
	
	public Scope() {
		assignments = new HashMap<String, ExpressionNode>();
	}
	
	private Scope(Map<String, ExpressionNode> s) {
		assignments = s;
	}
	
	public void put(String name, ExpressionNode e) {
		assignments.put(name, e);
	}
	
	public boolean containsKey(String name) {
		return assignments.containsKey(name);
	}
	
	public ExpressionNode get(String name) {
		return assignments.get(name);
	}
	
	public Scope clone() {
		Map<String, ExpressionNode> assigns = new HashMap<String, ExpressionNode>();
		
		for (String s : assignments.keySet()) {
			assigns.put(s, assignments.get(s));
		}
		
		return new Scope(assigns);
	}
}
