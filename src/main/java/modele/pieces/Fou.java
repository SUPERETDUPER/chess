package modele.pieces;

public class Fou extends DirectionPiece {
    private static final int[][] DIRECTIONS = {
            {-1, -1},
            {-1, 1},
            {1, -1},
            {1, 1}
    };


    public Fou(boolean isWhite) {
        super(isWhite);
    }

    @Override
    int[][] getDirections() {
        return DIRECTIONS;
    }

    @Override
    int unicodeForWhite() {
        return 9815;
    }

    @Override
    int unicodeForBlack() {
        return 9821;
    }

    @Override
    public int getValue() {
        return 3;
    }
}
