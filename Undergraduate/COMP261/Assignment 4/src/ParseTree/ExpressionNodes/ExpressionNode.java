package ParseTree.ExpressionNodes;

import java.util.Scanner;

import LookAheadScanner.LookAheadScanner;
import Main.ParserFailureException;
import ParseTree.AbstractNode;
import ParseTree.SenNodes.SenNode;

public abstract class ExpressionNode extends AbstractNode {
	
	public static ExpressionNode ParseExpression(LookAheadScanner s) {
		return ParseExpression(s, false);
	}
	
	public static ExpressionNode ParseExpression(LookAheadScanner s, boolean lookAhead) {
		if (s.hasNext("[0-9]+") || (s.hasNext("\\-") && s.hasNext("[0-9]+", 1))) {
			return NumNode.ParseNumNode(s);
		} else if (s.hasNext(OperatorNode.OPERATOR_PATTERN) || ((s.hasNext(OperatorNode.INFIX_OPERATOR_PATTERN, 1) || s.hasNext(OperatorNode.INFIX_OPERATOR_PATTERN, 4)) && !lookAhead)) {
			return OperatorNode.ParseOperatorNode(s);
		} else if (s.hasNext("fuelLeft|oppLR|oppFB|numBarrels|barrelLR|barrelFB|wallDist")) {
			return SenNode.ParseSenExpressionNode(s);
		} else if (s.hasNext("\\$[A-Za-z][A-Za-z0-9]*")) {
			return VariableNode.ParseVariableExpressionNode(s);
		}
		
		throw new ParserFailureException("No expression dected");
	}
	
}
