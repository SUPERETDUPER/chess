package model.pieces;

import model.moves.Move;
import model.util.Colour;
import model.util.Offset;

public class Queen extends DirectionPiece {
    private static final Offset[] OFFSETS = {
            Offset.TOP_LEFT,
            Offset.UP,
            Offset.TOP_RIGHT,
            Offset.LEFT,
            Offset.RIGHT,
            Offset.BOTTOM_LEFT,
            Offset.DOWN,
            Offset.BOTTOM_RIGHT
    };

    public Queen(Colour colour) {
        super(colour);
    }

    @Override
    Offset[] getDirections() {
        return OFFSETS;
    }

    @Override
    int getUnicodeWhite() {
        return 9813;
    }

    @Override
    int getUnicodeBlack() {
        return 9819;
    }

    @Override
    public int getUnsignedValue() {
        return 8;
    }

    @Override
    public void notifyMoveComplete(Move move) {
    }

    @Override
    public void notifyMoveUndo(Move move) {
    }

    @Override
    String getName() {
        return "Queen";
    }
}
