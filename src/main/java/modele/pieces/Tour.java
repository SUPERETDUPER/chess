package modele.pieces;

import modele.mouvement.Mouvement;
import modele.util.Couleur;
import modele.util.Offset;

public class Tour extends DirectionPiece {
    private static final Offset[] OFFSET = {
            Offset.HAUT,
            Offset.GAUCHE,
            Offset.DROIT,
            Offset.BAS
    };

    public Tour(Couleur couleur) {
        super(couleur);
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
    Offset[] getDirections() {
        return OFFSET;
    }

    @Override
    public int getValeurPositive() {
        return 5;
    }

    @Override
    public void notifyMoveCompleted(Mouvement mouvement) {
    }

    @Override
    public void notifyMoveUndo(Mouvement mouvement) {
    }

    @Override
    String getNom() {
        return "Tour";
    }
}
