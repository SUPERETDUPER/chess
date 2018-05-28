package modele.pieces;

public class Cavalier extends OffsetPiece {
    private static final int[][] OFFSETS = {
            {-1, -2},
            {-2, -1},
            {-2, 1},
            {-1, 2},
            {1, 2},
            {2, 1},
            {2, -1},
            {1, -2}
    };

    public Cavalier(Couleur couleur) {
        super(couleur);
    }

    @Override
    int[][] getOffsets() {
        return OFFSETS;
    }

    @Override
    int unicodeForWhite() {
        return 9816;
    }

    @Override
    int unicodeForBlack() {
        return 9822;
    }

    @Override
    public int getValue() {
        return 3;
    }
}
