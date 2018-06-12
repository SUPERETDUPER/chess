package model.pieces;

import model.moves.Move;
import model.util.Colour;
import model.util.Offset;

public class Knight extends OffsetPiece {
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

    public Knight(Colour colour) {
        super(colour);
    }

    @Override
    Offset[] getOffsets() {
        return OFFSETS;
    }

    @Override
    int getUnicodeWhite() {
        return 9816;
    }

    @Override
    int getUnicodeBlack() {
        return 9822;
    }

    @Override
    public int getUnsignedValue() {
        return 3;
    }

    @Override
    public void notifyMoveComplete(Move move) {
    }

    @Override
    public void notifyMoveUndo(Move move) {
    }

    @Override
    String getName() {
        return "Knight";
    }
}
