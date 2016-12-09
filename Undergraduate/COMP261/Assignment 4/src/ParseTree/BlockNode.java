package ParseTree;

import java.util.ArrayList;
import java.util.Scanner;

import LookAheadScanner.LookAheadScanner;
import Main.ParserFailureException;
import Main.Robot;
import Main.Scope;

public class BlockNode extends AbstractNode {

	private ArrayList<StatmentNode> statments;
	
	private BlockNode(ArrayList<StatmentNode> s) {
		statments = s;
	}
	
	@Override
	public int execute(Robot robot, Scope scope) {
		
		Scope fresh = scope.clone();
		
		for (StatmentNode s : statments) {
			s.execute(robot, fresh);
		}
		
		return 1;
	}
	
	public String toString() {
		StringBuilder b = new StringBuilder();
		
		b.append("{")
		 .append("\n");
		
		for (StatmentNode s : statments) {
			b.append(s.toString())
			 .append("\n");
		}
		
		b.append("}");
		
		return b.toString();
	}
	
	public static BlockNode ParseBlockNode(LookAheadScanner s) {
		if (!s.hasNext("\\{")) {
			throw new ParserFailureException("Expected '{' token");
		}
		s.next();
		
		ArrayList<StatmentNode> statments = new ArrayList<StatmentNode>();
		
		while (s.hasNext(StatmentNode.STATMENT_NODE_PATTERN)) {
			statments.add(StatmentNode.ParseStatmentNode(s));
		}
		
		if (statments.size() == 0) {
			throw new ParserFailureException("Block must contain atleast one statment");
		}
		
		if (!s.hasNext("\\}")) {
			throw new ParserFailureException("Expected '}' token");
		}
		s.next();
		
		return new BlockNode(statments);
	}

}
