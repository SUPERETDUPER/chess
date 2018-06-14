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

    /**
     * The number of moves that have been applied to this piece. Used to know if the piece has moved
     */
    private int numberOfAppliedMoves = 0;

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
        numberOfAppliedMoves += 1;
    }

    @Override
    public void notifyMoveUndo(Move move) {
        numberOfAppliedMoves -= 1;
    }

    boolean hasMoved() {
        return numberOfAppliedMoves != 0;
    }

    @Override
    String getName() {
        return "Rook";
    }
}
