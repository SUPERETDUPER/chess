package model.pieces;

import model.moves.Move;
import model.util.Colour;
import model.util.Offset;

/**
 * La pièce la dame
 */
public class Queen extends DirectionPiece {
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

    public Queen(Colour colour) {
        super(colour);
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
    public void notifyMoveCompleted(Move move) {
    }

    @Override
    public void notifyMoveUndo(Move move) {
    }

    @Override
    String getNom() {
        return "Queen";
    }
}
