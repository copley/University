package assignment3.chessview.pieces;

import assignment3.chessview.*;

public class Rook extends PieceImpl implements Piece {
	public Rook(boolean isWhite) {
		super(isWhite);
	}
	
	public boolean isValidMove(Position oldPosition, Position newPosition,
			Piece isTaken, Board board) {
		Piece p = board.pieceAt(oldPosition);
		Piece t = board.pieceAt(newPosition);
		
		return this.equals(p)
				&& (t == isTaken || (isTaken != null && isTaken.equals(t)))
				&& (board.clearColumnExcept(oldPosition, newPosition, p, t)
						|| board.clearRowExcept(oldPosition, newPosition, p, t));
	}
	
	public String toString() {
		if(isWhite) {
			return "R";
		} else {
			return "r";
		}
	}
}
