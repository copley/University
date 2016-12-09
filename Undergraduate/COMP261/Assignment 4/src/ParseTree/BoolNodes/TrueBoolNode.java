package ParseTree.BoolNodes;

import javax.xml.ws.handler.MessageContext.Scope;

import LookAheadScanner.LookAheadScanner;
import Main.ParserFailureException;
import Main.Robot;
import ParseTree.BoolNode;

public class TrueBoolNode extends BoolNode {

	private TrueBoolNode() {
		
	}
	
	@Override
	public int execute(Robot robot, Main.Scope scope) {
		return 1;
	}
	
	public static BoolNode ParseTrueBoolNode(LookAheadScanner s) {
		if (!s.hasNext("true")) {
			throw new ParserFailureException("Expected 'true' token");
		}
		
		return new TrueBoolNode();
	}
	
}
