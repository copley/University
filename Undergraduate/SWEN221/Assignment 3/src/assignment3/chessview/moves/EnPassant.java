package assignment3.chessview.moves;

import assignment3.chessview.*;
import assignment3.chessview.pieces.*;

/**
 * This represents an "en passant move" ---
 * http://en.wikipedia.org/wiki/En_passant.
 * 
 * @author djp
 * 
 */
public class EnPassant implements MultiPieceMove {
	private SinglePieceMove move;

	public EnPassant(SinglePieceMove move) {
		this.move = move;
	}

	public boolean isWhite() {
		return false;
	}

	public boolean isValid(Board board) {
		// Get the peices
		Piece moving = board.pieceAt(move.oldPosition);
		
		if (moving == null) {
			return false;
		}
		
		Piece taking = board.pieceAt(new Position(move.newPosition.row() + (moving.isWhite() ? -1 : +1),
				move.newPosition.column()));

		// Make sure the move is valid
		//if (!move.isValid(board)) {
		//	return false;
		//}

		if (!(moving instanceof Pawn) || !(taking instanceof Pawn)) {
			return false;
		}
		
		//Pawn p1 = (Pawn) moving;
		//Pawn p2 = (Pawn) taking;

		// Make sure we are moving from row 5
		if (move.oldPosition.row() != (moving.isWhite() ? 5 : 4)) {
			return false;
		}
//		if (move.oldPosition.row() != 5) {
//			return false;
//		}

		if (!((Pawn) taking).canEnPassant()) {
			return false;
		}

		return true;
	}

	public void apply(Board board) {
		Piece moving = board.pieceAt(move.oldPosition);
		
		board.setPieceAt(new Position(move.newPosition.row() + (moving.isWhite() ? -1 : +1),
				move.newPosition.column()), null);
		move.apply(board);
	}

	public String toString() {
		return "ep";
	}
}
