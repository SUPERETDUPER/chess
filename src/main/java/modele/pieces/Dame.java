package modele.pieces;

import modele.plateau.Offset;

/**
 * La pièce la dame
 */
public class Dame extends DirectionPiece {
    private static final Offset[] OFFSET = {
            Offset.HAUT_GAUGHE,
            Offset.HAUT_CENTRE,
            Offset.HAUT_DROIT,
            Offset.MILIEU_GAUCHE,
            Offset.MILIEU_DROIT,
            Offset.BAS_GAUCHE,
            Offset.BAS_CENTRE,
            Offset.BAS_DROIT
    };

    public Dame(Couleur couleur) {
        super(couleur);
    }

    @Override
    Offset[] getDirections() {
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

    @Override
    public int getValeurPositive() {
        return 8;
    }
}
