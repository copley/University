package ParseTree;

import java.util.Scanner;

import javax.xml.ws.handler.MessageContext.Scope;

import LookAheadScanner.LookAheadScanner;
import Main.ParserFailureException;
import Main.Robot;
import ParseTree.ExpressionNodes.ExpressionNode;
import ParseTree.ExpressionNodes.VariableNode;

public class AssignmentNode extends StatmentNode {

	private VariableNode variable;
	private ExpressionNode expression;
	
	private AssignmentNode(VariableNode v, ExpressionNode e) {
		variable = v;
		expression = e;
	}
	
	@Override
	public int execute(Robot robot, Main.Scope scope) {
		//VariableNode.assignments.put(variable, expression);
		scope.put(variable.toString(), expression);
		
		return 1;
	}
	
	public String toString() {
		StringBuilder b = new StringBuilder();
		
		b.append(variable.toString())
		 .append(" = ")
		 .append(expression.toString())
		 .append("\n");
		
		return b.toString();
	}
	
	public static AssignmentNode ParseAssignmentNode(LookAheadScanner s) {
		if (!s.hasNext(VariableNode.VARIABLE_PATTERN)) {
			throw new ParserFailureException("Was expecting a variable token");
		}
		
		VariableNode v = VariableNode.ParseVariableExpressionNode(s);
		
		if (!s.hasNext("=")) {
			throw new ParserFailureException("Was expecting the token '='");
		}
		s.next();
		
		ExpressionNode e = ExpressionNode.ParseExpression(s);
		
		if (!s.hasNext(";")) {
			throw new ParserFailureException("Was expecing ';'");
		}
		s.next();
		
		return new AssignmentNode(v, e);
	}

}
