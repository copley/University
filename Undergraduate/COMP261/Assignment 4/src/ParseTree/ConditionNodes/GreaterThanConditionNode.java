package ParseTree.ConditionNodes;

import java.util.Scanner;

import javax.xml.ws.handler.MessageContext.Scope;

import LookAheadScanner.LookAheadScanner;
import Main.ParserFailureException;
import Main.Robot;
import ParseTree.ConditionNode;
import ParseTree.ExpressionNodes.ExpressionNode;

public class GreaterThanConditionNode extends ConditionNode {

	private ExpressionNode node1;
	private ExpressionNode node2;
	
	private GreaterThanConditionNode(ExpressionNode n1, ExpressionNode n2) {
		node1 = n1;
		node2 = n2;
	}
	
	@Override
	public int execute(Robot robot, Main.Scope scope) {
		return (node1.execute(robot, scope) > node2.execute(robot, scope)) ? 1 : 0;
	}
	
	public String toString() {
		StringBuilder b = new StringBuilder();
		
		b.append("gt ( ")
		 .append(node1.toString())
		 .append(", ")
		 .append(node2.toString())
		 .append(")");
		
		return b.toString();
	}
	
	public static GreaterThanConditionNode ParseGreaterThanConditionNode(LookAheadScanner s) {
		if (s.hasNext("gt")) {
			return ParseGreaterThanConditionNodePreFix(s);
		} else if (s.hasNext(">", 1) || s.hasNext(">", 4)) {
			return ParseGreaterThanConditionNodeInFix(s);
		}
		
		throw new ParserFailureException("Unable to parse greated than node");
	}
	
	private static GreaterThanConditionNode ParseGreaterThanConditionNodePreFix(LookAheadScanner s) {
		if (!s.hasNext("gt")) {
			throw new ParserFailureException("Missing 'gt' token");
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
		
		return new GreaterThanConditionNode(n1, n2);
		
	}
	
	private static GreaterThanConditionNode ParseGreaterThanConditionNodeInFix(LookAheadScanner s) {
		ExpressionNode n1 = ExpressionNode.ParseExpression(s);
		
		if (!s.hasNext(">")) {
			throw new ParserFailureException("Expected '>' token");
		}
		s.next();
		
		ExpressionNode n2 = ExpressionNode.ParseExpression(s);
		
		return new GreaterThanConditionNode(n1, n2);
		
	}

}
