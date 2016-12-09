package ParseTree.ActionNodes;

import java.util.Scanner;

import javax.xml.ws.handler.MessageContext.Scope;

import LookAheadScanner.LookAheadScanner;
import Main.ParserFailureException;
import Main.Robot;

public class ShieldOffActionNode extends ActionNode {

	private ShieldOffActionNode() {
		
	}
	
	@Override
	public int execute(Robot robot, Main.Scope scope) {
		robot.setShield(false);
		
		return 1;
	}
	
	public String toString() {
		return "shieldOff;";
	}
	
	public static ShieldOffActionNode ParseShieldOffActionNode(LookAheadScanner s) {
		if (!s.hasNext("shieldOff")) {
			throw new ParserFailureException("Was expecting 'turnL' token");
		}
		s.next();
		
		if (!s.hasNext(";")) {
			throw new ParserFailureException("Was expecing ';'");
		}
		s.next();
		
		return new ShieldOffActionNode();
	}

}
