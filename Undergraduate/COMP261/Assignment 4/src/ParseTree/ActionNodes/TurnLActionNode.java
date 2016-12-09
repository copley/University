package ParseTree.ActionNodes;

import java.util.Scanner;

import javax.xml.ws.handler.MessageContext.Scope;

import LookAheadScanner.LookAheadScanner;
import Main.ParserFailureException;
import Main.Robot;

public class TurnLActionNode extends ActionNode {

	private TurnLActionNode() {
		
	}
	
	@Override
	public int execute(Robot robot, Main.Scope scope) {
		robot.turnLeft();
		
		return 1;
	}
	
	public String toString() {
		return "turnL;";
	}
	
	public static TurnLActionNode ParseTurnLActionNode(LookAheadScanner s) {
		if (!s.hasNext("turnL")) {
			throw new ParserFailureException("Was expecting 'turnL' token");
		}
		s.next();
		
		if (!s.hasNext(";")) {
			throw new ParserFailureException("Was expecing ';'");
		}
		s.next();
		
		return new TurnLActionNode();
	}

}
