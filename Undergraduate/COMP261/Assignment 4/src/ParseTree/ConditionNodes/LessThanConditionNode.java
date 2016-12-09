package ParseTree.ConditionNodes;

import java.util.Scanner;

import javax.xml.ws.handler.MessageContext.Scope;

import LookAheadScanner.LookAheadScanner;
import Main.ParserFailureException;
import Main.Robot;
import ParseTree.ConditionNode;
import ParseTree.ExpressionNodes.ExpressionNode;

public class LessThanConditionNode extends ConditionNode {

	private ExpressionNode node1;
	private ExpressionNode node2;
	
	private LessThanConditionNode(ExpressionNode n1, ExpressionNode n2) {
		node1 = n1;
		node2 = n2;
	}
	
	@Override
	public int execute(Robot robot, Main.Scope scope) {
		return (node1.execute(robot, scope) < node2.execute(robot, scope)) ? 1 : 0;
	}
	
	public String toString() {
		StringBuilder b = new StringBuilder();
		
		b.append("lt ( ")
		 .append(node1.toString())
		 .append(", ")
		 .append(node2.toString())
		 .append(")");
		
		return b.toString();
	}
	
	public static LessThanConditionNode ParseLessThanConditionNode(LookAheadScanner s) {
		if (s.hasNext("lt")) {
			return ParseLessThanConditionNodePreFix(s);
		} else if (s.hasNext("<", 1) || s.hasNext("<", 4)) {
			return ParseLessThanConditionNodeInFix(s);
		}
		
		throw new ParserFailureException("Unable to parse less than node");
	}
	
	private static LessThanConditionNode ParseLessThanConditionNodePreFix(LookAheadScanner s) {
		if (!s.hasNext("lt")) {
			throw new ParserFailureException("Missing 'lt' token");
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
		
		return new LessThanConditionNode(n1, n2);
		
	}
	
	private static LessThanConditionNode ParseLessThanConditionNodeInFix(LookAheadScanner s) {
		ExpressionNode n1 = ExpressionNode.ParseExpression(s);
		
		if (!s.hasNext("<")) {
			throw new ParserFailureException("Expected a comma");
		}
		s.next();
		
		ExpressionNode n2 = ExpressionNode.ParseExpression(s);
		
		return new LessThanConditionNode(n1, n2);
		
	}
	

}
