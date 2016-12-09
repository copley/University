package ParseTree.IfStatment;

import java.util.ArrayList;
import java.util.Scanner;

import javax.xml.ws.handler.MessageContext.Scope;

import LookAheadScanner.LookAheadScanner;
import Main.ParserFailureException;
import Main.Robot;
import ParseTree.BlockNode;
import ParseTree.ConditionNode;
import ParseTree.StatmentNode;

public class IfNode extends StatmentNode {

	private ConditionNode condition;
	private ArrayList<ElseIfNode> elseIfStatments;
	private ElseNode elseStatment;
	private BlockNode block;
	
	private IfNode(ConditionNode c, BlockNode b, ArrayList<ElseIfNode> eifs, ElseNode e) {
		condition = c;
		block = b;
		elseIfStatments = eifs;
		elseStatment = e;
	}
	
	@Override
	public int execute(Robot robot, Main.Scope scope) {
		if (condition.execute(robot, scope) == 1) {
			return block.execute(robot, scope);
		}
		
		// Go through the else if statments
		for (ElseIfNode n : elseIfStatments) {
			if (n.execute(robot, scope) == 1) {
				return 1;
			}
		}
		
		if (elseStatment != null && elseStatment.execute(robot, scope) == 1) {
			return 1;
		}
		
		return 0;
	}
	
	public static IfNode ParseIfNode(LookAheadScanner s) {
		if (!s.hasNext("if")) {
			throw new ParserFailureException("Expected 'if' token");
		}
		s.next();
		
		if (!s.hasNext("\\(")) {
			throw new ParserFailureException("Expected '(' token");
		}
		s.next();
		
		ConditionNode c = ConditionNode.ParseConditionNode(s);
		
		if (!s.hasNext("\\)")) {
			throw new ParserFailureException("Expected ')' token");
		}
		s.next();
		
		BlockNode b = BlockNode.ParseBlockNode(s);
		
		ArrayList<ElseIfNode> elseIf = new ArrayList<ElseIfNode>();
		while (s.hasNext("elif")) {
			elseIf.add(ElseIfNode.ParseElseIfNode(s));
		}

		ElseNode e = null;
		if (s.hasNext("else")) {
			e = ElseNode.ParseElseNode(s);
		}
		
		return new IfNode(c, b, elseIf, e);
	}
	
	public String toString() {
		StringBuilder b = new StringBuilder();
		
		b.append("if (")
		 .append(condition.toString())
		 .append(")")
		 .append(block.toString())
		 .append("\n");
		
		for (ElseIfNode e : elseIfStatments) {
			b.append(e.toString())
			 .append("\n");
		}
		
		if (elseStatment != null) {
			b.append(elseStatment.toString())
			 .append("\n");
		}
		
		return b.toString();
	}

}
