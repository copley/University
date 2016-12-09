package ParseTree.ActionNodes;

import java.util.Scanner;

import javax.xml.ws.handler.MessageContext.Scope;

import LookAheadScanner.LookAheadScanner;
import Main.ParserFailureException;
import Main.Robot;

public class TakeFuelActionNode extends ActionNode {

	private TakeFuelActionNode() {
		
	}
	
	@Override
	public int execute(Robot robot, Main.Scope scope) {
		robot.takeFuel();
		return 1;
	}
	
	public String toString() {
		return "takeFuel;";
	}
	
	public static TakeFuelActionNode ParseTakeFuelActionNode(LookAheadScanner s) {
		if (!s.hasNext("takeFuel")) {
			throw new ParserFailureException("Was expecting 'turnL' token");
		}
		s.next();
		
		if (!s.hasNext(";")) {
			throw new ParserFailureException("Was expecing ';'");
		}
		s.next();
		
		return new TakeFuelActionNode();
	}

}
