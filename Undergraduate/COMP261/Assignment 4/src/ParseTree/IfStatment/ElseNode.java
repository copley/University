package ParseTree.IfStatment;

import java.util.Scanner;

import javax.xml.ws.handler.MessageContext.Scope;

import LookAheadScanner.LookAheadScanner;
import Main.ParserFailureException;
import Main.Robot;
import ParseTree.BlockNode;
import ParseTree.StatmentNode;

public class ElseNode extends StatmentNode {

	private BlockNode block;
	
	private ElseNode(BlockNode b) {
		block = b;
	}
	
	@Override
	public int execute(Robot robot, Main.Scope scope) {
		return block.execute(robot, scope);
	}
	
	public String toString() {
		StringBuilder b = new StringBuilder();
		
		b.append("else")
		 .append(block.toString());
		
		return b.toString();
	}

	public static ElseNode ParseElseNode(LookAheadScanner s) {
		if (!s.hasNext("else")) {
			throw new ParserFailureException("Expected else token");
		}
		s.next();
		
		BlockNode b = BlockNode.ParseBlockNode(s);
		
		return new ElseNode(b);
	}
}
