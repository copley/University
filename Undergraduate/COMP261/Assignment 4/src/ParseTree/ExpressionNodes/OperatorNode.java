package ParseTree.ExpressionNodes;

import java.util.Scanner;

import LookAheadScanner.LookAheadScanner;
import Main.ParserFailureException;
import ParseTree.OperatorNodes.AddOperatorNode;
import ParseTree.OperatorNodes.DivOperatorNode;
import ParseTree.OperatorNodes.MulOperatorNode;
import ParseTree.OperatorNodes.SubOperatorNode;

public abstract class OperatorNode extends ExpressionNode{

	public static final String OPERATOR_PATTERN = "add|sub|mul|div";
	public static final String INFIX_OPERATOR_PATTERN = "\\+|-|\\*|/";
	
	public static OperatorNode ParseOperatorNode(LookAheadScanner s) {
		if (s.hasNext("add") || s.hasNext("\\+", 1) || s.hasNext("\\+", 4)) {
			return AddOperatorNode.ParseAddOperatorNode(s);
		} else if (s.hasNext("sub") || s.hasNext("-", 1) || s.hasNext("\\-", 4)) {
			return SubOperatorNode.ParseSubOperatorNode(s);
		} else if (s.hasNext("mul") || s.hasNext("\\*", 1) || s.hasNext("\\*", 4)) {
			return MulOperatorNode.ParseMulOperatorNode(s);
		} else if (s.hasNext("div") || s.hasNext("/", 1) || s.hasNext("/", 4)) {
			return DivOperatorNode.ParseDivOperatorNode(s);
		}
		
		throw new ParserFailureException("Not able to parse an operator");
	}
}
