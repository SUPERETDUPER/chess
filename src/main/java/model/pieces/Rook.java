package model.pieces;

import model.moves.Move;
import model.util.Colour;
import model.util.Offset;

public class Rook extends DirectionPiece {
    private static final Offset[] OFFSET = {
            Offset.HAUT,
            Offset.GAUCHE,
            Offset.DROIT,
            Offset.BAS
    };

    public Rook(Colour colour) {
        super(colour);
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
    public void notifyMoveCompleted(Move move) {
    }

    @Override
    public void notifyMoveUndo(Move move) {
    }

    @Override
    String getNom() {
        return "Rook";
    }
}
