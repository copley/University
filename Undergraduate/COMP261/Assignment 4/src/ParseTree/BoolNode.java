package ParseTree;

import LookAheadScanner.LookAheadScanner;
import Main.ParserFailureException;
import ParseTree.BoolNodes.FalseBoolNode;
import ParseTree.BoolNodes.TrueBoolNode;

public abstract class BoolNode extends ConditionNode {

	public static final String BOOL_PATTERN = "true|false";
	
	public static BoolNode ParseBoolNode(LookAheadScanner s) {
		if (s.hasNext("true")) {
			return TrueBoolNode.ParseTrueBoolNode(s);
		} else if (s.hasNext("false")) {
			return FalseBoolNode.ParseFalseBoolNode(s);
		}
		
		throw new ParserFailureException("Unable to parse boolean node");
	}
	
}
