package ParseTree.SenNodes;

import LookAheadScanner.LookAheadScanner;
import Main.ParserFailureException;
import Main.Robot;
import Main.Scope;

public class FuelLeftSenNode extends SenNode {

	public static SenNode ParseFuelLeftSenNode(LookAheadScanner s) {
		if (!s.hasNext("fuelLeft")) {
			throw new ParserFailureException("Was expecting 'fuelLeft' token");
		}
		s.next();
		
		return new FuelLeftSenNode();
	}
	
	private FuelLeftSenNode() {
		
	}

	@Override
	public int execute(Robot robot, Scope scope) {
		return robot.getFuel();
	}

	public String toString() {
		return "fuelLeft";
	}
	
}
