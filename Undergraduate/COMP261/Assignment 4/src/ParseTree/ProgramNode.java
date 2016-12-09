package ParseTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import LookAheadScanner.LookAheadScanner;
import Main.ParserFailureException;
import Main.Robot;
import Main.Scope;
import ParseTree.ExpressionNodes.ExpressionNode;

public class ProgramNode extends AbstractNode {

	private ArrayList<StatmentNode> statments;
	
	private ProgramNode(ArrayList<StatmentNode> s) {
		statments = s;
	}
	
	@Override
	public int execute(Robot robot, Scope scope) {
		
		if (scope == null) {
			scope = new Scope();
		}
		
		for (StatmentNode s : statments) {
			s.execute(robot, scope);
		}
		
		return 1;
	}
	
	public String toString() {
		StringBuilder b = new StringBuilder();
		
		for (StatmentNode s : statments) {
			b.append(s.toString())
			 .append("\n");
		}
		
		return b.toString();
	}
	
	public static ProgramNode ParseProgramNode(LookAheadScanner s) {
		ArrayList<StatmentNode> statments = new ArrayList<StatmentNode>();
		
		while (s.hasNext(StatmentNode.STATMENT_NODE_PATTERN)) {
			statments.add(StatmentNode.ParseStatmentNode(s));
		}
		
		// There shouldent be anything left in the scanner
		if (s.hasNext()) {
			throw new ParserFailureException("Unable to parse eveything");
		}
		
		return new ProgramNode(statments);
	}

}
