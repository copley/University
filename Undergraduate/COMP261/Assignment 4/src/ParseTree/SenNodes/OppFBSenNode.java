package ParseTree.SenNodes;

import java.util.Scanner;

import javax.xml.ws.handler.MessageContext.Scope;

import LookAheadScanner.LookAheadScanner;
import Main.ParserFailureException;
import Main.Robot;

public class OppFBSenNode extends SenNode {

	public static SenNode ParsesOppFBSenNode(LookAheadScanner s) {
		if (!s.hasNext("oppFB")) {
			throw new ParserFailureException("Expected oppFB token");
		}
		
		s.next();
		
		return new OppFBSenNode();
	}
	
	private OppFBSenNode() {
		
	}

	@Override
	public int execute(Robot robot, Main.Scope scope) {
		return robot.getOpponentFB();
	}

	public String toString() {
		return "oppFB";
	}
	
}
