package modele.pieces;

//TODO Implement castling
public class Roi extends OffsetPiece {
    private static final int[][] OFFSETS = {
            {-1, -1},
            {-1, 0},
            {-1, 1},
            {0, -1},
            {0, 1},
            {1, -1},
            {1, 0},
            {1, 1}
    };

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

    @Override
    int[][] getOffsets() {
        return OFFSETS;
    }
}