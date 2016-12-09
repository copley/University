package assignment3.chessview.pieces;

import assignment3.chessview.*;

public class Bishop extends PieceImpl implements Piece {
	public Bishop(boolean isWhite) {
		super(isWhite);
	}
	
	public boolean isValidMove(Position oldPosition, Position newPosition,
			Piece isTaken, Board board) {
		int diffCol = Math.max(oldPosition.column(), newPosition.column())
				- Math.min(oldPosition.column(), newPosition.column());
		int diffRow = Math.max(oldPosition.row(), newPosition.row())
				- Math.min(oldPosition.row(), newPosition.row());
		Piece p = board.pieceAt(oldPosition);
		Piece t = board.pieceAt(newPosition);
		
		return this.equals(p) && (diffCol == diffRow) 
				&& (t == isTaken || (isTaken != null && isTaken.equals(t)))
				&& (board.clearDiaganolExcept(oldPosition, newPosition, p, t));
						//|| board.clearColumnExcept(oldPosition, newPosition, p,
						//		t) || board.clearRowExcept(oldPosition,
						//newPosition, p, t));				
	}
	
	public String toString() {
		if(isWhite) {
			return "B";
		} else {
			return "b";
		}
	}
}
