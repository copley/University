package assignment3.chessview.pieces;

import java.util.Arrays;

import assignment3.chessview.*;



public abstract class PieceImpl {
	protected boolean isWhite;
	protected boolean hasMoved;
	
	public PieceImpl(boolean isWhite) {		
		this.isWhite = isWhite;
	}

	public boolean isWhite() {
		return isWhite;
	}
	
	public boolean hasMoved() {
		return hasMoved;
	}
	
	public void moved() {
		hasMoved = true;
	}
	
	public boolean equals(Object o) {
		if (o instanceof PieceImpl) {
			PieceImpl p = (PieceImpl) o;
			return o.getClass() == getClass() && isWhite == p.isWhite;
		}
		return false;
	}		
}
