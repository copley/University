package ParseTree.SenNodes;

import java.util.Scanner;

import javax.xml.ws.handler.MessageContext.Scope;

import LookAheadScanner.LookAheadScanner;
import Main.ParserFailureException;
import Main.Robot;

public class OppLRSenNode extends SenNode {

	public static SenNode ParseOppLRSenNode(LookAheadScanner s) {
		if (!s.hasNext("oppLR")) {
			throw new ParserFailureException("Expected fuelLeft token");
		}
		
		s.next();
		
		return new OppLRSenNode();
	}
	
	private OppLRSenNode() {
		
	}

	@Override
	public int execute(Robot robot, Main.Scope scope) {
		return robot.getOpponentLR();
	}
	
	public String toString() {
		return "oppLR";
	}

}
