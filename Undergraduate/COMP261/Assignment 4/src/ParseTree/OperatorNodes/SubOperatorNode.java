package ParseTree.OperatorNodes;

import java.util.Scanner;

import javax.xml.ws.handler.MessageContext.Scope;

import LookAheadScanner.LookAheadScanner;
import Main.ParserFailureException;
import Main.Robot;
import ParseTree.ExpressionNodes.ExpressionNode;
import ParseTree.ExpressionNodes.OperatorNode;

public class SubOperatorNode extends OperatorNode {

	private ExpressionNode node1;
	private ExpressionNode node2;
	
	public static OperatorNode ParseSubOperatorNode(LookAheadScanner s) {
		if (s.hasNext("sub")) {
			return ParseSubOperatorNodePreFix(s);
		} else if (s.hasNext("-", 1) || s.hasNext("-", 4)) {
			return ParseSubOperatorNodeInFix(s);
		}
		
		throw new ParserFailureException("Unable to parse sub operator");
	}
	
	public static OperatorNode ParseSubOperatorNodePreFix(LookAheadScanner s) {
		if (!s.hasNext("sub")) {
			throw new ParserFailureException("Expected div token");
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
		
		return new SubOperatorNode(n1, n2);
	}
	
	public static OperatorNode ParseSubOperatorNodeInFix(LookAheadScanner s) {
		ExpressionNode n1 = ExpressionNode.ParseExpression(s, true);
		
		if (!s.hasNext("-")) {
			throw new ParserFailureException("Expected a - token");
		}
		s.next();
		
		ExpressionNode n2 = ExpressionNode.ParseExpression(s);
		
		return new SubOperatorNode(n1, n2);
	}
	
	private SubOperatorNode(ExpressionNode n1, ExpressionNode n2) {
		node1 = n1;
		node2 = n2;
	}

	@Override
	public int execute(Robot robot, Main.Scope scope) {
		return node1.execute(robot, scope) - node2.execute(robot, scope);
	}
	
	public String toString() {
		StringBuilder b = new StringBuilder();
		
		b.append("sub(")
		 .append(node1.toString())
		 .append(",")
		 .append(node2.toString())
		 .append(")");
		
		return b.toString();
	}

}
