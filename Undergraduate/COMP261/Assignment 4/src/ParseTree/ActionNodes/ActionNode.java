package ParseTree.ActionNodes;

import java.util.Scanner;

import LookAheadScanner.LookAheadScanner;
import Main.ParserFailureException;
import ParseTree.StatmentNode;

public abstract class ActionNode extends StatmentNode {

	public static final String ACTION_PATTERN = "loop|if|while|move|turnL|turnR|turnAround|shieldOn|shieldOff|takeFuel|wait|";
	
	public static StatmentNode ParseActionNode(LookAheadScanner s) {
		if (s.hasNext("move")) {
			return MoveActionNode.ParseMoveAction(s);
		} else if (s.hasNext("turnL")) {
			return TurnLActionNode.ParseTurnLActionNode(s);
		} else if (s.hasNext("turnR")) {
			return TurnRActionNode.ParseTurnRActionNode(s);
		} else if (s.hasNext("turnAround")) {
			return TurnAroundActionNode.ParseTurnAroundActionNode(s);
		} else if (s.hasNext("shieldOn")) {
			return ShieldOnActionNode.ParseShieldOnActionNode(s);
		} else if (s.hasNext("shieldOff")) {
			return ShieldOffActionNode.ParseShieldOffActionNode(s);
		} else if (s.hasNext("takeFuel")) {
			return TakeFuelActionNode.ParseTakeFuelActionNode(s);
		} else if (s.hasNext("wait")) {
			return WaitActionNode.ParseWaitActionNode(s);
		}
		
		throw new ParserFailureException("Unable to parse action");
	}

}
