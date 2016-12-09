package ParseTree.SenNodes;

import LookAheadScanner.LookAheadScanner;
import Main.ParserFailureException;
import Main.Robot;
import ParseTree.ExpressionNodes.ExpressionNode;

public class BarrelFBSenNode extends SenNode {

	private ExpressionNode expression;

	public static SenNode ParseBarrelFBSenNode(LookAheadScanner s) {
		if (!s.hasNext("barrelFB")) {
			throw new ParserFailureException("Expected barrelFB token");
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

		return new BarrelFBSenNode(en);
	}

	private BarrelFBSenNode(ExpressionNode e) {
		expression = e;
	}

	@Override
	public int execute(Robot robot, Main.Scope scope) {
		int expressionResult = (expression == null) ? 0 : expression.execute(robot, scope);

		return robot.getBarrelFB(expressionResult);
	}
	
	public String toString() {
		StringBuilder b = new StringBuilder();
		
		b.append("barrelFB");
		
		// Add the expression
		if (expression != null) {
			b.append("(").append(expression.toString()).append(")");
		}
		
		return b.toString();
	}

}
