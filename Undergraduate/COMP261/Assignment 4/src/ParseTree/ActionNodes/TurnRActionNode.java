package ParseTree.ActionNodes;

import java.util.Scanner;

import javax.xml.ws.handler.MessageContext.Scope;

import LookAheadScanner.LookAheadScanner;
import Main.ParserFailureException;
import Main.Robot;

public class TurnRActionNode extends ActionNode {

	private TurnRActionNode() {
		
	}
	
	@Override
	public int execute(Robot robot, Main.Scope scope) {
		robot.turnRight();
		return 1;
	}
	
	public String toString() {
		return "turnR;";
	}
	
	public static TurnRActionNode ParseTurnRActionNode(LookAheadScanner s) {
		if (!s.hasNext("turnR")) {
			throw new ParserFailureException("Was expecting 'turnL' token");
		}
		s.next();
		
		if (!s.hasNext(";")) {
			throw new ParserFailureException("Was expecing ';'");
		}
		s.next();
		
		return new TurnRActionNode();
	}
	
}
