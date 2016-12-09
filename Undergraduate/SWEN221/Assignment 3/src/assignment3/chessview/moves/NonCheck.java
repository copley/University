package assignment3.chessview.moves;

import assignment3.chessview.*;
import assignment3.chessview.pieces.*;

public class NonCheck implements Move {
	private MultiPieceMove move;	
	
	public NonCheck(MultiPieceMove move) {
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
		
		return !tmp.isInCheck(isWhite()) && !tmp.isInCheck(!isWhite());
	}
	
	public void apply(Board board) {
		move.apply(board);
	}
	
	public String toString() {
		return move.toString();
	}
}
