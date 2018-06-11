package modele.pieces;

import modele.mouvement.Mouvement;
import modele.util.Couleur;
import modele.util.Offset;

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

    @Override
    public void notifyMoveCompleted(Mouvement mouvement) {
    }

    @Override
    public void notifyMoveUndo(Mouvement mouvement) {
    }

    @Override
    String getNom() {
        return "Cavalier";
    }
}
