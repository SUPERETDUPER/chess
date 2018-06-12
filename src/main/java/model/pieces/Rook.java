package model.pieces;

import model.moves.Move;
import model.util.Colour;
import model.util.Offset;

public class Rook extends DirectionPiece {
    private static final Offset[] OFFSETS = {
            Offset.UP,
            Offset.LEFT,
            Offset.RIGHT,
            Offset.DOWN
    };

    public Rook(Colour colour) {
        super(colour);
    }

    @Override
    int getUnicodeWhite() {
        return 9814;
    }

    @Override
    int getUnicodeBlack() {
        return 9820;
    }

    @Override
    Offset[] getDirections() {
        return OFFSETS;
    }

    @Override
    public int getUnsignedValue() {
        return 5;
    }

    @Override
    public void notifyMoveComplete(Move move) {
    }

    @Override
    public void notifyMoveUndo(Move move) {
    }

    @Override
    String getName() {
        return "Rook";
    }
}
