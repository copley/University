package ParseTree.OperatorNodes;

import java.util.Scanner;

import javax.xml.ws.handler.MessageContext.Scope;

import LookAheadScanner.LookAheadScanner;
import Main.ParserFailureException;
import Main.Robot;
import ParseTree.ExpressionNodes.ExpressionNode;
import ParseTree.ExpressionNodes.OperatorNode;

public class AddOperatorNode extends OperatorNode {

	private ExpressionNode node1;
	private ExpressionNode node2;
	
	@Override
	public int execute(Robot robot, Main.Scope scope) {
		return node1.execute(robot, scope) + node2.execute(robot, scope);
	}

	public String toString() {
		StringBuilder b = new StringBuilder();
		
		b.append("add(")
		 .append(node1.toString())
		 .append(",")
		 .append(node2.toString())
		 .append(")");
		
		return b.toString();
	}
	
	
	public static OperatorNode ParseAddOperatorNode(LookAheadScanner s) {
		if (s.hasNext("add")) {
			return ParseAddOperatorNodePreFix(s);
		} else if (s.hasNext("\\+", 1) || s.hasNext("\\+", 4)) {
			return ParseAddOperatorNodeInFix(s);
		}
		
		throw new ParserFailureException("Unable to parse add operator");
	}
	
	private static OperatorNode ParseAddOperatorNodePreFix(LookAheadScanner s) {
		if (!s.hasNext("add")) {
			throw new ParserFailureException("Expected add token");
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
		
		return new AddOperatorNode(n1, n2);
	}
	
	private static OperatorNode ParseAddOperatorNodeInFix(LookAheadScanner s) {
		ExpressionNode n1 = ExpressionNode.ParseExpression(s, true);
		
		if (!s.hasNext("\\+")) {
			throw new ParserFailureException("Expecting '+' token");
		}
		s.next();
		
		ExpressionNode n2 = ExpressionNode.ParseExpression(s);
		
		return new AddOperatorNode(n1, n2);
	}

	private AddOperatorNode(ExpressionNode n1, ExpressionNode n2) {
		node1 = n1;
		node2 = n2;
	}
}
