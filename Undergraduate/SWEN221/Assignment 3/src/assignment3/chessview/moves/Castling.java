package assignment3.chessview.moves;

import assignment3.chessview.*;
import assignment3.chessview.pieces.*;

public class Castling implements MultiPieceMove {	
	private boolean kingSide;
	private boolean isWhite;
	
	public Castling(boolean isWhite, boolean kingSide) {		
		this.kingSide = kingSide;
		this.isWhite = isWhite;
	}
	
	public boolean isWhite() {
		return isWhite;
	}
	
	public void apply(Board board) {
		// Get the current positions
		Position kingPosition = isWhite ? new Position(1, 5) : new Position(8, 5);
		Position rookPosition = kingSide ? new Position(kingPosition.row(), 8) : new Position(kingPosition.row(), 1);
		Position newKingPosition;
		Position newRookPosition;
		
		// Calculate the new positions
		if (kingSide) {
			newKingPosition = new Position(rookPosition.row(), rookPosition.column()-1);
			newRookPosition = new Position(newKingPosition.row(), newKingPosition.column()-1);
		} else {
			newKingPosition = new Position(rookPosition.row(), rookPosition.column()+2);
			newRookPosition = new Position(newKingPosition.row(), newKingPosition.column()+1);
		}
		
		// Apply the move
		board.move(kingPosition, newKingPosition);
		board.move(rookPosition, newRookPosition);
	}
	
	public boolean isValid(Board board) {
		Position kingPosition = isWhite ? new Position(1, 5) : new Position(8, 5);
		Position rookPosition = kingSide ? new Position(kingPosition.row(), 8) : new Position(kingPosition.row(), 1);
		Piece king = board.pieceAt(kingPosition);
		Piece rook = board.pieceAt(rookPosition);
		
		// TODO: Make sure that the king isnt in check
		if (king instanceof King && rook instanceof Rook) {
			// Make sure that nether of the pieces have been moved
			if (king.hasMoved() || rook.hasMoved()) {
				return false;
			}
			
			return board.clearRowExcept(kingPosition, rookPosition, king, rook);
		}
		
		return false;		
	}		
	
	public String toString() {
		if(kingSide) {
			return "O-O";
		} else {
			return "O-O-O";
		}
	}
}
