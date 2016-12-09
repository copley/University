package ParseTree.BoolNodes;

import javax.xml.ws.handler.MessageContext.Scope;

import LookAheadScanner.LookAheadScanner;
import Main.ParserFailureException;
import Main.Robot;
import ParseTree.BoolNode;

public class FalseBoolNode extends BoolNode {

	private FalseBoolNode() {

	}

	@Override
	public int execute(Robot robot, Main.Scope scope) {
		return 0;
	}

	public static BoolNode ParseFalseBoolNode(LookAheadScanner s) {
		if (!s.hasNext("false")) {
			throw new ParserFailureException("Expected 'false' token");
		}

		return new FalseBoolNode();
	}

}
