package modele.pieces;

public class Tour extends RepeatOffsetPiece {
    private static final int[][] OFFSET = {
            {-1, 0},
            {0, 1},
            {1, 0},
            {0, -1}
    };

    public Tour(boolean isWhite) {
        super(isWhite);
    }

    @Override
    int unicodeForWhite() {
        return 9814;
    }

    @Override
    int unicodeForBlack() {
        return 9820;
    }

    @Override
    int[][] getRepeatOffset() {
        return OFFSET;
    }
}
