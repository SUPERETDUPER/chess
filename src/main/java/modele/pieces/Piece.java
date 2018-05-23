package modele.pieces;

import modele.board.Board;
import modele.moves.Action;

public abstract class Piece {
    private final boolean isWhite;

    public Piece(boolean isWhite) {
        this.isWhite = isWhite;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public String getUnicode() {
        return Character.toString((char) (isWhite ? unicodeForWhite() : unicodeForBlack()));
    }

    abstract int unicodeForWhite();

    abstract int unicodeForBlack();

    abstract Action[] getPossibleActions(Board board);
}
