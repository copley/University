package ParseTree.SenNodes;

import LookAheadScanner.LookAheadScanner;
import Main.ParserFailureException;
import Main.Robot;

public class WallDistSenNode extends SenNode {

	
	public static WallDistSenNode ParseWallDistSenNode(LookAheadScanner s) {
		if (!s.hasNext("wallDist")) {
			throw new ParserFailureException("Expected wallDist token");
		}
		
		s.next();
		
		return new WallDistSenNode();
	}
	
	private WallDistSenNode() {
		
	}

	@Override
	public int execute(Robot robot, Main.Scope scope) {
		return robot.getDistanceToWall();
		
	}
	
	public String toString() {
		return "wallDist";
	}
}
