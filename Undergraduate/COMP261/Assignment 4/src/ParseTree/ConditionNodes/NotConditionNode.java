package ParseTree.ConditionNodes;

import java.util.Scanner;

import javax.xml.ws.handler.MessageContext.Scope;

import LookAheadScanner.LookAheadScanner;
import Main.ParserFailureException;
import Main.Robot;
import ParseTree.ConditionNode;

public class NotConditionNode extends ConditionNode {

	private ConditionNode condition;
	
	private NotConditionNode(ConditionNode c) {
		condition = c;
	}
	
	@Override
	public int execute(Robot robot, Main.Scope scope) {
		return (condition.execute(robot, scope) == 1) ? 0 : 1;
	}
	
	public String toString() {
		StringBuilder b = new StringBuilder();
		
		b.append("not ( ")
		 .append(condition.toString())
		 .append(")");
		
		return b.toString();
	}
	
	public static NotConditionNode ParseNotConditionNode(LookAheadScanner s) {
		if (s.hasNext("not")) {
			return ParseNotConditionNodeNonSymbol(s);
		} else if (s.hasNext("!")) {
			return ParseNotConditionNodeSymbol(s);
		}
		
		throw new ParserFailureException("Unable to parse not node");
	}
	
	private static NotConditionNode ParseNotConditionNodeNonSymbol(LookAheadScanner s) {
		if (!s.hasNext("not")) {
			throw new ParserFailureException("Missing 'not' token");
		}
		s.next();
		
		if (!s.hasNext("\\(")) {
			throw new ParserFailureException("Expected the folowing token '('");
		}
		s.next();
		
		ConditionNode c = ConditionNode.ParseConditionNode(s);
		
		if (!s.hasNext("\\)")) {
			throw new ParserFailureException("Expected the folowing toekn ')'");
		}
		s.next();
		
		return new NotConditionNode(c);
	}

	private static NotConditionNode ParseNotConditionNodeSymbol(LookAheadScanner s) {
		if (!s.hasNext("!")) {
			throw new ParserFailureException("Missing '!' token");
		}
		s.next();
		
		ConditionNode c = ConditionNode.ParseConditionNode(s);
		
		return new NotConditionNode(c);
	}

}
