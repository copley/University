package ParseTree;

import java.util.Scanner;

import LookAheadScanner.LookAheadScanner;
import Main.ParserFailureException;
import ParseTree.ActionNodes.ActionNode;
import ParseTree.IfStatment.IfNode;

public abstract class StatmentNode extends AbstractNode {
	
	public static final String STATMENT_NODE_PATTERN = "loop|if|while|move|turnL|turnR|turnAround|shieldOn|shieldOff|takeFuel|wait|\\$[A-Za-z][A-Za-z0-9]*";

	public static StatmentNode ParseStatmentNode(LookAheadScanner s) {
		if (s.hasNext("loop")) {
			return LoopNode.ParseLoopNode(s);
		} else if (s.hasNext("if")) {
			return IfNode.ParseIfNode(s);
		} else if (s.hasNext("while")) {
			return WhileNode.ParseWhileNode(s);
		} else if (s.hasNext("\\$[A-Za-z][A-Za-z0-9]*")) {
			return AssignmentNode.ParseAssignmentNode(s);
		} else if (s.hasNext(ActionNode.ACTION_PATTERN)) {
			return ActionNode.ParseActionNode(s);
		}
		
		throw new ParserFailureException("Unable to parse statent node");
	}
	
}
