package modele.pieces;

import modele.Couleur;
import modele.moves.Mouvement;
import modele.plateau.Offset;

public class Fou extends DirectionPiece {
    private static final Offset[] DIRECTIONS = {
            Offset.HAUT_GAUGHE,
            Offset.HAUT_DROIT,
            Offset.BAS_GAUCHE,
            Offset.BAS_DROIT
    };


    public Fou(Couleur couleur) {
        super(couleur);
    }

    @Override
    Offset[] getDirections() {
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
    public int getValeurPositive() {
        return 3;
    }

    @Override
    public void notifyMoveCompleted(Mouvement mouvement) {

    }

    @Override
    public void notifyMoveUndo(Mouvement mouvement) {

    }

    @Override
    String getNom() {
        return "Fou";
    }
}
