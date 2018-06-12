package modele.pieces;

import modele.mouvement.Mouvement;
import modele.util.Couleur;
import modele.util.Offset;

/**
 * La pièce la dame
 */
public class Reine extends DirectionPiece {
    private static final Offset[] OFFSET = {
            Offset.HAUT_GAUGHE,
            Offset.HAUT,
            Offset.HAUT_DROIT,
            Offset.GAUCHE,
            Offset.DROIT,
            Offset.BAS_GAUCHE,
            Offset.BAS,
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

    @Override
    public void notifyMoveCompleted(Mouvement mouvement) {
    }

    @Override
    public void notifyMoveUndo(Mouvement mouvement) {
    }

    @Override
    String getNom() {
        return "Reine";
    }
}
