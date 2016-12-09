package ParseTree.ConditionNodes;

import javax.xml.ws.handler.MessageContext.Scope;

import LookAheadScanner.LookAheadScanner;
import Main.ParserFailureException;
import Main.Robot;
import ParseTree.ConditionNode;
import ParseTree.ExpressionNodes.ExpressionNode;

public class NotEqualToConditionNode extends ConditionNode {

	private ExpressionNode expression1;
	private ExpressionNode expression2;
	
	private NotEqualToConditionNode(ExpressionNode e1, ExpressionNode e2) {
		expression1 = e1;
		expression2 = e2;
	}

	@Override
	public int execute(Robot robot, Main.Scope scope) {
		return (expression1.execute(robot, scope) != expression2.execute(robot, scope)) ? 1 : 0;
	}
	
	public String toString() {
		StringBuilder b = new StringBuilder();
		
		b.append(expression1.toString())
		 .append(" != ")
		 .append(expression2.toString());
		
		return b.toString();
	}
	
	public static ConditionNode ParseNotEqualToConditionNode(LookAheadScanner s) {
		ExpressionNode n1 = ExpressionNode.ParseExpression(s);
		
		if (!s.hasNext("!=")) {
			throw new ParserFailureException("Expected the token '=='");
		}
		s.next();
		
		ExpressionNode n2 = ExpressionNode.ParseExpression(s);
		
		return new NotEqualToConditionNode(n1, n2);
	}

}
