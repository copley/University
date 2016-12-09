package ParseTree.SenNodes;

import LookAheadScanner.LookAheadScanner;
import Main.ParserFailureException;
import Main.Robot;
import Main.Scope;
import ParseTree.ExpressionNodes.ExpressionNode;

public class BarrelLRSenNode extends SenNode {

	private ExpressionNode expression;
	
	public static SenNode ParseBarrelLRSenNode(LookAheadScanner s) {
		if (!s.hasNext("barrelLR")) {
			throw new ParserFailureException("Expected barrelLR token");
		}
		
		s.next();
		
		ExpressionNode en = null;
		// Check for the opternal expression
		if (s.hasNext("\\(")) {
			s.next();
			
			en = ExpressionNode.ParseExpression(s);
			
			if (!s.hasNext("\\)")) {
				throw new ParserFailureException("Expected closing bracket ')'");
			}
			s.next();
		}
		
		return new BarrelLRSenNode(en);
	}
	
	private BarrelLRSenNode(ExpressionNode e) {
		expression = e;
	}

	@Override
	public int execute(Robot robot, Scope scope) {
		int expressionResult = (expression == null) ? 0 : expression.execute(robot, scope);
		
		return robot.getBarrelLR(expressionResult);
	}
	
	public String toString() {
		StringBuilder b = new StringBuilder();
		
		b.append("barrelLR");
		
		// Add the expression
		if (expression != null) {
			b.append("(").append(expression.toString()).append(")");
		}
		
		return b.toString();
	}

}
