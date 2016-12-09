package ParseTree.ExpressionNodes;

import java.util.HashMap;
import java.util.Scanner;

import LookAheadScanner.LookAheadScanner;
import Main.ParserFailureException;
import Main.Robot;
import Main.Scope;

public class VariableNode extends ExpressionNode {

	public static final String VARIABLE_PATTERN = "\\$[A-Za-z][A-Za-z0-9]*";
	//public static final Map<VariableNode, ExpressionNode> assignments = new HashMap<>();
	
	private String name;
	
	public static VariableNode ParseVariableExpressionNode(LookAheadScanner s) {
		if (!s.hasNext(VARIABLE_PATTERN)) {
			throw new ParserFailureException("Expected a varaible name token");
		}
		
		String name = s.next();
		
		return new VariableNode(name);
	}
	
	private VariableNode(String n) {
		name = n;
	}
	
	@Override
	public int execute(Robot robot, Scope scope) {
		if (!scope.containsKey(name)) {
			throw new ParserFailureException("Variable " + name + " has not been declared");
		}
		
		return scope.get(name).execute(robot, scope);
		//return (assignments.get(this) != null) ? assignments.get(this).execute(robot, null) : 0;
	}
	
	public String toString() {
		return name;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		VariableNode other = (VariableNode) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}

}
