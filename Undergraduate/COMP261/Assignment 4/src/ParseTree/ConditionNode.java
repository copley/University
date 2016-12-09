package ParseTree;

import LookAheadScanner.LookAheadScanner;
import Main.ParserFailureException;
import ParseTree.ConditionNodes.AndConditionNode;
import ParseTree.ConditionNodes.EqualToConditionNode;
import ParseTree.ConditionNodes.GreaterThanConditionNode;
import ParseTree.ConditionNodes.GreaterThanEqualToConditionNode;
import ParseTree.ConditionNodes.LessThanConditionNode;
import ParseTree.ConditionNodes.LessThanEqualToConditionNode;
import ParseTree.ConditionNodes.NotConditionNode;
import ParseTree.ConditionNodes.NotEqualToConditionNode;
import ParseTree.ConditionNodes.OrConditionNode;
import ParseTree.ExpressionNodes.ExpressionNode;

public abstract class ConditionNode extends AbstractNode {
	
	public static ConditionNode ParseConditionNode(LookAheadScanner s) {
		
		if (s.hasNext(BoolNode.BOOL_PATTERN)) {
			return BoolNode.ParseBoolNode(s);
		} else if ((s.hasNext("<", 1) && s.hasNext("=", 2)) || (s.hasNext("<", 4) && s.hasNext("=", 5))) { 
			return LessThanEqualToConditionNode.ParseLessThanEqualToConditionNode(s);
		} else if (s.hasNext("not") || s.hasNext("!")) {
			return NotConditionNode.ParseNotConditionNode(s);
		} else if (s.hasNext("lt") || s.hasNext("<", 1) || s.hasNext("<", 4)) {
			return LessThanConditionNode.ParseLessThanConditionNode(s);
		} else if ((s.hasNext(">", 1) && s.hasNext("=", 2)) || (s.hasNext(">", 4) && s.hasNext("=", 5))) { 
			return GreaterThanEqualToConditionNode.ParseGreaterThanEqualToConditionNode(s);
		} else if (s.hasNext("gt") || s.hasNext(">", 1) || s.hasNext(">", 4)) {
			return GreaterThanConditionNode.ParseGreaterThanConditionNode(s);
		} else if (s.hasNext("eq") || s.hasNext("==", 1) || s.hasNext("==", 4)) {
			return EqualToConditionNode.ParseEqualToConditionNode(s);
		} else if (s.hasNext("!=", 1) || s.hasNext("!=", 4)) { 
			return NotEqualToConditionNode.ParseNotEqualToConditionNode(s);
		} else if (s.hasNext("and") || s.hasNext("&&", 1) || s.hasNext("&&", 4)) {
			return AndConditionNode.ParseAndConditionNode(s);
		} else if (s.hasNext("or") || s.hasNext("||, 1") || s.hasNext("||", 4)) {
			return OrConditionNode.ParseOrConditionNode(s);
		}
		
		throw new ParserFailureException("Couldent parse condition node");
	}

}
