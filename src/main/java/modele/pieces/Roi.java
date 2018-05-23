package modele.pieces;

public class Roi extends Piece {
    public Roi(boolean isWhite) {
        super(isWhite);
    }

    @Override
    int unicodeForWhite() {
        return 9812;
    }

    @Override
    int unicodeForBlack() {
        return 9818;
    }
}
