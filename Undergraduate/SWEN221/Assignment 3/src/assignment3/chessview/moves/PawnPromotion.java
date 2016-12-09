package assignment3.chessview.moves;

import assignment3.chessview.*;
import assignment3.chessview.pieces.*;

/**
 * This represents a pawn promotion.
 * @author djp
 *
 */
public class PawnPromotion implements MultiPieceMove {
	private Piece promotion;
	private SinglePieceMove move;
	
	public PawnPromotion(SinglePieceMove move, Piece promotion) {
		this.promotion = promotion;
		this.move = move;
	}
	
	public boolean isWhite() {
		return move.isWhite();
	}
	
	public boolean isValid(Board board) {
		if (!move.isValid(board)) {
			return false;
		}
		
		if (!(move.piece instanceof Pawn) || (promotion instanceof Pawn)) {
			return false;
		}
		
		if (promotion == null || promotion.isWhite() != move.piece.isWhite()) {
			return false;
		}
		
		if (move.isWhite() && move.newPosition.row() == 8) {
 			return true;
		} else if (!move.isWhite() && move.newPosition.row() == 1) {
			return true;
		}
		
		return false;
	}
	
	public void apply(Board board) {
		move.apply(board);
		board.setPieceAt(move.newPosition, promotion);
	}
	
	public String toString() {
		return super.toString() + "=" + SinglePieceMove.pieceChar(promotion);
	}
}
