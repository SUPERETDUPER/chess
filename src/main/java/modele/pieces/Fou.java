package modele.pieces;

public class Fou extends RepeatOffsetPiece {
    private static final int[][] OFFSET = {
            {-1, -1},
            {-1, 1},
            {1, -1},
            {1, 1}
    };


    public Fou(boolean isWhite) {
        super(isWhite);
    }

    @Override
    int[][] getRepeatOffset() {
        return OFFSET;
    }

    @Override
    int unicodeForWhite() {
        return 9815;
    }

    @Override
    int unicodeForBlack() {
        return 9821;
    }
}
