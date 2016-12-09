package ParseTree.ConditionNodes;

import java.util.Scanner;

import javax.xml.ws.handler.MessageContext.Scope;

import LookAheadScanner.LookAheadScanner;
import Main.ParserFailureException;
import Main.Robot;
import ParseTree.ConditionNode;

public class OrConditionNode extends ConditionNode {

	private ConditionNode condition1;
	private ConditionNode condition2;
	
	private OrConditionNode(ConditionNode c1, ConditionNode c2) {
		condition1 = c1;
		condition2 = c2;
	}
	
	@Override
	public int execute(Robot robot, Main.Scope scope) {
		return (condition1.execute(robot, scope) == 1 || condition2.execute(robot, scope) == 1) ? 1 : 0;
	}
	
	public String toString() {
		StringBuilder b = new StringBuilder();
		
		b.append("or ( ")
		 .append(condition1.toString())
		 .append(", ")
		 .append(condition2.toString())
		 .append(")");
		
		return b.toString();
	}
	
	public static OrConditionNode ParseOrConditionNode(LookAheadScanner s) {
		if (s.hasNext("or")) {
			return ParseOrConditionNodePreFix(s);
		} else if (s.hasNext("||", 1) || s.hasNext("||", 4)) {
			return ParseOrConditionNodeInFix(s);
		}
		
		throw new ParserFailureException("Unable to parse or node");
	}
	
	private static OrConditionNode ParseOrConditionNodePreFix(LookAheadScanner s) {
		if (!s.hasNext("or")) {
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
		
		return new OrConditionNode(c1, c2);
	}

	private static OrConditionNode ParseOrConditionNodeInFix(LookAheadScanner s) {
		ConditionNode c1 = ConditionNode.ParseConditionNode(s);
		
		if (!s.hasNext("||")) {
			throw new ParserFailureException("Expected a comma");
		}
		s.next();
		
		ConditionNode c2 = ConditionNode.ParseConditionNode(s);
		
		return new OrConditionNode(c1, c2);
	}

	
}
