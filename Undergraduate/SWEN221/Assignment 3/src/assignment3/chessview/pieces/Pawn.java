package assignment3.chessview.pieces;

import assignment3.chessview.*;

public class Pawn extends PieceImpl implements Piece {
	private boolean enpassant = false;
	
	public Pawn(boolean isWhite) {
		super(isWhite);
	}
	
	public boolean isValidMove(Position oldPosition, Position newPosition,
			Piece isTaken, Board board) {
		int dir = isWhite ? 1 : -1;
		int oldRow = oldPosition.row();
		int oldCol = oldPosition.column();
		int newRow = newPosition.row();
		int newCol = newPosition.column();

		Piece p = board.pieceAt(oldPosition);
		Piece t = board.pieceAt(newPosition);

		// this logic should be more complex than for other pieces, since there
		// is a difference between a take and non-take move for pawns.
		if (isTaken != null && isTaken.equals(t)) {
			int rowDiff = newRow - oldRow;
			int colDiff = newCol - oldCol;
			return ((colDiff == 1 || colDiff == -1) && rowDiff == dir);
		} else if ((oldRow + dir) == newRow && oldCol == newCol) {
			return this.equals(p) && t == null;
		} else if ((oldRow + dir + dir) == newRow && oldCol == newCol) {
			return ((dir == 1 && oldRow == 2) || (dir == -1 && oldRow == 7))					
					&& t == null && this.equals(p) && board.pieceAt(new Position(oldRow + dir, oldCol)) == null;
		}
		return false;
	}
	
	public boolean canEnPassant() {
		return enpassant;
	}
	
	public void allowEnPassant() {
		enpassant = true;
	}
	
	public void disallowEnPassant() {
		enpassant = false;
	}
	
	public String toString() {
		if(isWhite) {
			return "P";
		} else {
			return "p";
		}
	}
}
