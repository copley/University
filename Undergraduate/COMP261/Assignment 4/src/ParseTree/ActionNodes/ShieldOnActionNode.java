package ParseTree.ActionNodes;

import java.util.Scanner;

import javax.xml.ws.handler.MessageContext.Scope;

import LookAheadScanner.LookAheadScanner;
import Main.ParserFailureException;
import Main.Robot;

public class ShieldOnActionNode extends ActionNode {

	private ShieldOnActionNode() {
		
	}
	
	@Override
	public int execute(Robot robot, Main.Scope scope) {
		robot.setShield(true);
		return 1;
	}
	
	public String toString() {
		return "shieldOn;";
	}
	
	public static ShieldOnActionNode ParseShieldOnActionNode(LookAheadScanner s) {
		if (!s.hasNext("shieldOn")) {
			throw new ParserFailureException("Was expecting 'turnL' token");
		}
		s.next();
		
		if (!s.hasNext(";")) {
			throw new ParserFailureException("Was expecing ';'");
		}
		s.next();
		
		return new ShieldOnActionNode();
	}

}
