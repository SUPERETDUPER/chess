package modele.pieces;

import modele.Couleur;
import modele.moves.Mouvement;
import modele.plateau.Offset;

/**
 * La pièce la dame
 */
public class Reine extends DirectionPiece {
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

    public Reine(Couleur couleur) {
        super(couleur);
    }

    @Override
    Offset[] getDirections() {
        return OFFSET;
    }

    @Override
    public int unicodeForWhite() {
        return 9813;
    }

    @Override
    public int unicodeForBlack() {
        return 9819;
    }

    @Override
    public int getValeurPositive() {
        return 8;
    }

    @Override
    public void notifyMoveCompleted(Mouvement mouvement) {

    }

    @Override
    public void notifyMoveUndo(Mouvement mouvement) {

    }
}
