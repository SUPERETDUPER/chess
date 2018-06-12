package model.pieces;

import model.moves.Move;
import model.util.Colour;
import model.util.Offset;

public class Bishop extends DirectionPiece {
    private static final Offset[] DIRECTIONS = {
            Offset.HAUT_GAUGHE,
            Offset.HAUT_DROIT,
            Offset.BAS_GAUCHE,
            Offset.BAS_DROIT
    };


    public Bishop(Colour colour) {
        super(colour);
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
    public void notifyMoveCompleted(Move move) {

    }

    @Override
    public void notifyMoveUndo(Move move) {

    }

    @Override
    String getNom() {
        return "Bishop";
    }
}
