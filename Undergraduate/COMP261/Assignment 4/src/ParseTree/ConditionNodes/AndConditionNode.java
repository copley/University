package ParseTree.ConditionNodes;

import java.util.Scanner;

import javax.xml.ws.handler.MessageContext.Scope;

import LookAheadScanner.LookAheadScanner;
import Main.ParserFailureException;
import Main.Robot;
import ParseTree.ConditionNode;

public class AndConditionNode extends ConditionNode {

	private ConditionNode condition1;
	private ConditionNode condition2;
	
	private AndConditionNode(ConditionNode c1, ConditionNode c2) {
		condition1 = c1;
		condition2 = c2;
	}
	
	@Override
	public int execute(Robot robot, Main.Scope scope) {
		return (condition1.execute(robot, scope) == 1 && condition2.execute(robot, scope) == 1) ? 1 : 0;
	}
	
	public String toString() {
		StringBuilder b = new StringBuilder();
		
		b.append("and ( ")
		 .append(condition1.toString())
		 .append(", ")
		 .append(condition2.toString())
		 .append(")");
		
		return b.toString();
	}
	
	public static AndConditionNode ParseAndConditionNode(LookAheadScanner s) {
		if (s.hasNext("and")) {
			return ParseAndConditionNodePreFix(s);
		} else if (s.hasNext("&&", 1) || s.hasNext("&&", 4)) {
			return ParseAndConditionNodeInFix(s);
		}
		
		throw new ParserFailureException("Unable to parse and node");
	}
	
	private static AndConditionNode ParseAndConditionNodePreFix(LookAheadScanner s) {
		if (!s.hasNext("and")) {
			throw new ParserFailureException("Missing 'and' token");
		}
		s.next();
		
		if (!s.hasNext("\\(")) {
			throw new ParserFailureException("Expected the folowing token '('");
		}
		s.next();
		
		ConditionNode c1 = ConditionNode.ParseConditionNode(s);
		
		if (!s.hasNext(",")) {
			throw new ParserFailureException("Expected a comma");
		}
		s.next();
		
		ConditionNode c2 = ConditionNode.ParseConditionNode(s);
		
		if (!s.hasNext("\\)")) {
			throw new ParserFailureException("Expected the folowing toekn ')'");
		}
		s.next();
		
		return new AndConditionNode(c1, c2);
	}
	
	private static AndConditionNode ParseAndConditionNodeInFix(LookAheadScanner s) {
		ConditionNode c1 = ConditionNode.ParseConditionNode(s);
		
		if (!s.hasNext("&&")) {
			throw new ParserFailureException("Expected the token '&&'");
		}
		s.next();
		
		ConditionNode c2 = ConditionNode.ParseConditionNode(s);
		
		return new AndConditionNode(c1, c2);
	}

}
