package model.pieces;

import model.moves.Move;
import model.util.Colour;
import model.util.Offset;
import org.jetbrains.annotations.NotNull;

public class Bishop extends DirectionPiece {
    private static final Offset[] DIRECTIONS = {
            Offset.TOP_LEFT,
            Offset.TOP_RIGHT,
            Offset.BOTTOM_LEFT,
            Offset.BOTTOM_RIGHT
    };


    public Bishop(Colour colour) {
        super(colour);
    }

    @NotNull
    @Override
    Offset[] getDirections() {
        return DIRECTIONS;
    }

    @Override
    int getUnicodeWhite() {
        return 9815;
    }

    @Override
    int getUnicodeBlack() {
        return 9821;
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

    @NotNull
    @Override
    String getName() {
        return "Bishop";
    }
}
