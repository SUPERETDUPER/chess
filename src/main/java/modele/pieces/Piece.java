package modele.pieces;

public abstract class Piece {
    private final boolean isWhite;

    public Piece(boolean isWhite) {
        this.isWhite = isWhite;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public String getUnicode() {
        return "\\u" + (isWhite ? unicodeForWhite() : unicodeForBlack());
    }

    abstract int unicodeForWhite();

    abstract int unicodeForBlack();
}
