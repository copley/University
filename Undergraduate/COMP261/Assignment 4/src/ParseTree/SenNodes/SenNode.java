package ParseTree.SenNodes;

import java.util.Scanner;

import LookAheadScanner.LookAheadScanner;
import Main.ParserFailureException;
import ParseTree.AbstractNode;
import ParseTree.ExpressionNodes.ExpressionNode;
import ParseTree.SenNodes.WallDistSenNode;

public abstract class SenNode extends ExpressionNode {
	
	public static SenNode ParseSenExpressionNode(LookAheadScanner s) {
		if (s.hasNext("fuelLeft")) {
			return FuelLeftSenNode.ParseFuelLeftSenNode(s);
		} else if (s.hasNext("oppLR")) {
			return OppLRSenNode.ParseOppLRSenNode(s);
		} else if (s.hasNext("oppFB")) {
			return OppFBSenNode.ParsesOppFBSenNode(s);
		} else if (s.hasNext("numBarrels")) {
			return NumBarrelsSenNode.ParseNumBarrelsSenNode(s);
		} else if (s.hasNext("barrelLR")) {
			return BarrelLRSenNode.ParseBarrelLRSenNode(s);
		} else if (s.hasNext("barrelFB")) {
			return BarrelFBSenNode.ParseBarrelFBSenNode(s);
		} else if (s.hasNext("wallDist")) {
			return WallDistSenNode.ParseWallDistSenNode(s);
		}
		
		throw new ParserFailureException("Sen token expected");

	}

}
