package ParseTree.IfStatment;

import java.util.Scanner;

import javax.xml.ws.handler.MessageContext.Scope;

import LookAheadScanner.LookAheadScanner;
import Main.ParserFailureException;
import Main.Robot;
import ParseTree.BlockNode;
import ParseTree.ConditionNode;
import ParseTree.StatmentNode;

public class ElseIfNode extends StatmentNode {

	private ConditionNode condition;
	private BlockNode block;
	
	private ElseIfNode(ConditionNode c, BlockNode b) {
		condition = c;
		block = b;
	}
	
	@Override
	public int execute(Robot robot, Main.Scope scope) {
		if (condition.execute(robot, scope) == 1) {
			block.execute(robot, scope);
			
			return 1;
		}
		
		return 0;
	}
	
	public String toString() {
		StringBuilder b = new StringBuilder();
		
		b.append("elfi")
		 .append("(")
		 .append(condition.toString())
		 .append(")")
		 .append("\n");
		
		b.append(block.toString())
		 .append("\n");
		
		return b.toString();
	}
	
	public static ElseIfNode ParseElseIfNode(LookAheadScanner s) {
		if (!s.hasNext("elif")) {
			throw new ParserFailureException("Expected elif token");
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
		
		BlockNode n = BlockNode.ParseBlockNode(s);
		
		return new ElseIfNode(c, n);
	}
}
