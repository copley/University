package ParseTree;

import Main.Robot;
import Main.Scope;



/**
 * Interface for all nodes that can be executed,
 * including the top level program node
 */

public interface RobotProgramNode {
	public int execute(Robot robot, Scope scope);
}
