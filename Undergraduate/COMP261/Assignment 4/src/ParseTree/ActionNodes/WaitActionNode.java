package ParseTree.ActionNodes;

import java.util.Scanner;

import javax.xml.ws.handler.MessageContext.Scope;

import LookAheadScanner.LookAheadScanner;
import Main.ParserFailureException;
import Main.Robot;
import ParseTree.ExpressionNodes.ExpressionNode;

public class WaitActionNode extends ActionNode {

	private ExpressionNode expression;
	
	private WaitActionNode(ExpressionNode n) {
		expression = n;
	}
	
	@Override
	public int execute(Robot robot, Main.Scope scope) {
		int result = (expression == null) ? 1 : expression.execute(robot, null);
		
		for (int i = 0; i < result; i++) {
			robot.idleWait();
		}
		
		return 1;
	}
	
	public String toString() {
		StringBuilder b = new StringBuilder();
		
		b.append("wait");
		
		if (expression != null) {
			b.append("(")
			 .append(expression.toString())
			 .append(")");
		}
		
		b.append(";");
		
		return b.toString();
	}
	
	public static WaitActionNode ParseWaitActionNode(LookAheadScanner s) {
		if (!s.hasNext("wait")) {
			throw new ParserFailureException("move token expected");
		}
		s.next();
		
		ExpressionNode en = null;
		// If there is an opternal paramater
		if (s.hasNext("\\(")) {
			s.next();
			en = ExpressionNode.ParseExpression(s);
			
			if (!s.hasNext("\\)")) {
				throw new ParserFailureException("Was expecting ')' token");
			}
			s.next();
		}
		
		if (!s.hasNext(";")) {
			throw new ParserFailureException("Was expecing ';'");
		}
		s.next();
		
		return new WaitActionNode(en);
	}

}
