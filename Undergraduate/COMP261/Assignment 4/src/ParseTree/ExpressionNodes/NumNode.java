package ParseTree.ExpressionNodes;

import java.util.Scanner;

import javax.xml.ws.handler.MessageContext.Scope;

import LookAheadScanner.LookAheadScanner;
import Main.ParserFailureException;
import Main.Robot;

public class NumNode extends ExpressionNode {

	private int value;
	
	private NumNode(int i) {
		value = i;
	}
	
	@Override
	public int execute(Robot robot, Main.Scope scope) {
		return value;
	}

	public String toString() {
		return String.format("%d", value);
	}
	
	public static NumNode ParseNumNode(LookAheadScanner s) {
		if (s.hasNext("-?[0-9]+")) {
			return new NumNode(Integer.parseInt(s.next()));
		} else if (s.hasNext("\\-") && s.hasNext("[0-9]+", 1)) {
			s.next();
			return new NumNode(-1 * Integer.parseInt(s.next()));
		}
		
		throw new ParserFailureException("Number token expected");
	}
}
