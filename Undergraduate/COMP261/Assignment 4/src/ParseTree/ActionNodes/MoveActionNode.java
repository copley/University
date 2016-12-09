package ParseTree.ActionNodes;

import java.util.Scanner;

import javax.xml.ws.handler.MessageContext.Scope;

import LookAheadScanner.LookAheadScanner;
import Main.ParserFailureException;
import Main.Robot;
import ParseTree.ExpressionNodes.ExpressionNode;

public class MoveActionNode extends ActionNode {

	private ExpressionNode node;
	
	private MoveActionNode (ExpressionNode e) {
		node = e;
	}
	
	@Override
	public int execute(Robot robot, Main.Scope scope) {
		int result = (node == null) ? 1 : node.execute(robot, scope);
				
		for (int i = 0; i < result; i++) {
			robot.move();
		}
		
		return 1;
	}

	public String toString() {
		StringBuilder b = new StringBuilder();
		
		b.append("move");
		
		if (node != null) {
			b.append("(")
			 .append(node.toString())
			 .append(")");
		}
		
		b.append(";");
		
		return b.toString();
	}
	
	public static MoveActionNode ParseMoveAction(LookAheadScanner s) {
		if (!s.hasNext("move")) {
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
		
		return new MoveActionNode(en);
	}

}
