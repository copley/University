package ParseTree;

import java.util.ArrayList;
import java.util.List;

import Main.Robot;
import Main.Scope;

public abstract class AbstractNode implements RobotProgramNode {

	public List<AbstractNode> childrent;
	
	public AbstractNode() {
		childrent = new ArrayList<AbstractNode>();
	}
	
	@Override
	public abstract int execute(Robot robot, Scope scope);

}
