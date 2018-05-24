package modele.pieces;

public class Dame extends RepeatOffsetPiece {
    private static final int[][] OFFSET = {
            {-1, -1},
            {-1, 1},
            {1, -1},
            {1, 1},
            {-1, 0},
            {0, 1},
            {1, 0},
            {0, -1}
    };


    public Dame(boolean isWhite) {
        super(isWhite);
    }

    @Override
    int[][] getRepeatOffset() {
        return OFFSET;
    }

    @Override
    int unicodeForWhite() {
        return 9813;
    }

    @Override
    int unicodeForBlack() {
        return 9819;
    }
}
