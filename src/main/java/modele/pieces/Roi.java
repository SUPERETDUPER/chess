package modele.pieces;

import modele.plateau.Offset;

//TODO Implement castling
public class Roi extends OffsetPiece {
    private static final Offset[] OFFSETS = {
            Offset.HAUT_GAUGHE,
            Offset.HAUT_CENTRE,
            Offset.HAUT_DROIT,
            Offset.MILIEU_GAUCHE,
            Offset.MILIEU_DROIT,
            Offset.BAS_GAUCHE,
            Offset.BAS_CENTRE,
            Offset.BAS_DROIT
    };

    public Roi(Couleur couleur) {
        super(couleur);
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
    Offset[] getOffsets() {
        return OFFSETS;
    }

    @Override
    public int getValeurPositive() {
        return 1000;
    }
}