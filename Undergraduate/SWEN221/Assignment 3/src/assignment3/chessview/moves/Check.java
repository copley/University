package assignment3.chessview.moves;

import assignment3.chessview.*;
import assignment3.chessview.pieces.*;

/**
 * This represents a "check move". Note that, a check move can only be made up
 * from an underlying simple move; that is, we can't check a check move.
 * 
 * @author djp
 * 
 */
public class Check implements Move {
	private MultiPieceMove move;	
	
	public Check(MultiPieceMove move) {
		this.move = move;		
	}
	
	public MultiPieceMove move() {
		return move;
	}
	
	public boolean isWhite() {
		return move.isWhite();
	}
	
	public boolean isValid(Board board) {
		if (!move.isValid(board)) {
			return false;
		}
		
		Board tmp = new Board(board);
		tmp.apply(move);
		
		//if (tmp.isInCheck(!isWhite())) {
			
		//}
		
		return tmp.isInCheck(!isWhite());
	}
	
	public void apply(Board board) {
		move.apply(board);
	}
	
	public String toString() {
		return move.toString() + "+";
	}
}
