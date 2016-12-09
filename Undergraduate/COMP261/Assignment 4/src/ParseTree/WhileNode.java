package ParseTree;

import java.util.Scanner;

import javax.xml.ws.handler.MessageContext.Scope;

import LookAheadScanner.LookAheadScanner;
import Main.ParserFailureException;
import Main.Robot;

public class WhileNode extends StatmentNode {

	private ConditionNode condition;
	private BlockNode block;
	
	public WhileNode(ConditionNode cn, BlockNode bn) {
		condition = cn;
		block = bn;
	}
	
	@Override
	public int execute(Robot robot, Main.Scope scope) {
		while(condition.execute(robot, scope) == 1) {
			block.execute(robot, scope);
		}
		
		return 1;
	}
	
	public String toString() {
		StringBuilder b = new StringBuilder();
		
		b.append("while (")
		 .append(condition.toString())
		 .append(")")
		 .append(block.toString());
		
		return b.toString();
	}
	
	public static WhileNode ParseWhileNode(LookAheadScanner s) {
		if (!s.hasNext("while")) {
			throw new ParserFailureException("Expecting token 'while'");
		}
		s.next();
		
		if (!s.hasNext("\\(")) {
			throw new ParserFailureException("Expected '(' token");
		}
		s.next();
		
		ConditionNode c = ConditionNode.ParseConditionNode(s);
		
		if (!s.hasNext("\\)")) {
			throw new ParserFailureException("Expected ')' token");
		}
		s.next();
		
		BlockNode b = BlockNode.ParseBlockNode(s);
		
		return new WhileNode(c, b);
	}

}
