package ParseTree.ActionNodes;

import java.util.Scanner;

import javax.xml.ws.handler.MessageContext.Scope;

import LookAheadScanner.LookAheadScanner;
import Main.ParserFailureException;
import Main.Robot;

public class TurnAroundActionNode extends ActionNode {

	private TurnAroundActionNode() {
		
	}
	
	@Override
	public int execute(Robot robot, Main.Scope scope) {
		robot.turnAround();
		
		return 1;
	}
	
	public String toString() {
		return "turnAround;";
	}
	
	public static TurnAroundActionNode ParseTurnAroundActionNode(LookAheadScanner s) {
		if (!s.hasNext("turnAround")) {
			throw new ParserFailureException("Was expecting 'turnAround' token");
		}
		s.next();
		
		if (!s.hasNext(";")) {
			throw new ParserFailureException("Was expecing ';'");
		}
		s.next();
		
		return new TurnAroundActionNode();
	}

}
