package ParseTree.SenNodes;

import LookAheadScanner.LookAheadScanner;
import Main.ParserFailureException;
import Main.Robot;
import Main.Scope;

public class NumBarrelsSenNode extends SenNode {

	public static SenNode ParseNumBarrelsSenNode(LookAheadScanner s) {
		if (!s.hasNext("numBarrels")) {
			throw new ParserFailureException("Expected numBarrels token");
		}
		
		s.next();
		
		return new NumBarrelsSenNode();
	}
	
	private NumBarrelsSenNode() {
		
	}

	@Override
	public int execute(Robot robot, Scope scope) {
		return robot.numBarrels();
	}

	public String toString() {
		return "numBarrels";
	}
	
}
