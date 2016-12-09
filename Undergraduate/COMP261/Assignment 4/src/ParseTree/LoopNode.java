package ParseTree;

import java.util.Scanner;

import javax.xml.ws.handler.MessageContext.Scope;

import LookAheadScanner.LookAheadScanner;
import Main.ParserFailureException;
import Main.Robot;

public class LoopNode extends StatmentNode {

	private BlockNode block;
	
	private LoopNode(BlockNode b) {
		block = b;
	}

	@Override
	public int execute(Robot robot, Main.Scope scope) {
		while (true) {
			block.execute(robot, scope);
		}
	}
	
	public String toString() {
		StringBuilder b = new StringBuilder();
		
		b.append("loop")
		 .append(block.toString())
		 .append("\n");
		
		return b.toString();
	}
	
	public static LoopNode ParseLoopNode(LookAheadScanner s) {
		if (!s.hasNext("loop")) {
			throw new ParserFailureException("Was expecting 'loop' token");
		}
		s.next();
		
		BlockNode b = BlockNode.ParseBlockNode(s);
		
		return new LoopNode(b);
	}
	
}
