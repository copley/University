package ParseTree.ConditionNodes;

import java.util.Scanner;

import javax.xml.ws.handler.MessageContext.Scope;

import LookAheadScanner.LookAheadScanner;
import Main.ParserFailureException;
import Main.Robot;
import ParseTree.ConditionNode;
import ParseTree.ExpressionNodes.ExpressionNode;

public class EqualToConditionNode extends ConditionNode {

	private ExpressionNode node1;
	private ExpressionNode node2;
	
	private EqualToConditionNode(ExpressionNode n1, ExpressionNode n2) {
		node1 = n1;
		node2 = n2;
	}
	
	@Override
	public int execute(Robot robot, Main.Scope scope) {
		return (node1.execute(robot, scope) == node2.execute(robot, scope)) ? 1 : 0;
	}
	
	public String toString() {
		StringBuilder b = new StringBuilder();
		
		b.append("eq ( ")
		 .append(node1.toString())
		 .append(", ")
		 .append(node2.toString())
		 .append(")");
		
		return b.toString();
	}
	
	public static EqualToConditionNode ParseEqualToConditionNode(LookAheadScanner s) {
		if (s.hasNext("eq")) {
			return ParseEqualToConditionNodePreFix(s);
		} else if (s.hasNext("==", 1) || s.hasNext("==", 4)) {
			return ParseEqualToConditionNodeInFix(s);
		}
		
		throw new ParserFailureException("Unable to parse equal to node");
	}
	
	private static EqualToConditionNode ParseEqualToConditionNodePreFix(LookAheadScanner s) {
		if (!s.hasNext("eq")) {
			throw new ParserFailureException("Missing 'eq' token");
		}
		s.next();
		
		if (!s.hasNext("\\(")) {
			throw new ParserFailureException("Expected the folowing token '('");
		}
		s.next();
		
		ExpressionNode n1 = ExpressionNode.ParseExpression(s);
		
		if (!s.hasNext(",")) {
			throw new ParserFailureException("Expected a comma");
		}
		s.next();
		
		ExpressionNode n2 = ExpressionNode.ParseExpression(s);
		
		if (!s.hasNext("\\)")) {
			throw new ParserFailureException("Expected the folowing toekn ')'");
		}
		s.next();
		
		return new EqualToConditionNode(n1, n2);
	}
	
	private static EqualToConditionNode ParseEqualToConditionNodeInFix(LookAheadScanner s) {
		ExpressionNode n1 = ExpressionNode.ParseExpression(s);
		
		if (!s.hasNext("==")) {
			throw new ParserFailureException("Expected the token '=='");
		}
		s.next();
		
		ExpressionNode n2 = ExpressionNode.ParseExpression(s);
		
		return new EqualToConditionNode(n1, n2);
	}
}
