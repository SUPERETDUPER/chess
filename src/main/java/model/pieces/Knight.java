package model.pieces;

import model.moves.Move;
import model.util.Colour;
import model.util.Offset;

public class Knight extends OffsetPiece {
    /**
     * The Offsets where the Knight can be moved. In an L shape
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
