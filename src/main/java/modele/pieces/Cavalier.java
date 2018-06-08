package modele.pieces;

import modele.Couleur;
import modele.plateau.Offset;

public class Cavalier extends OffsetPiece {
    /**
     * Les valeurs où le cavalier peut se déplacer
     * En forme de L
     */
    private static final Offset[] OFFSETS = {
            new Offset(-1, -2),
            new Offset(-2, -1),
            new Offset(-2, 1),
            new Offset(-1, 2),
            new Offset(1, 2),
            new Offset(2, 1),
            new Offset(2, -1),
            new Offset(1, -2)
    };

    public Cavalier(Couleur couleur) {
        super(couleur);
    }

    @Override
    Offset[] getOffsets() {
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
    public int getValeurPositive() {
        return 3;
    }
}
