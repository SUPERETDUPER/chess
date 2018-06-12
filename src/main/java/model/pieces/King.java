package model.pieces;

import model.moves.BaseMove;
import model.moves.CombineMove;
import model.moves.Move;
import model.util.BoardMap;
import model.util.Colour;
import model.util.Offset;
import model.util.Position;

import java.util.Collection;

//TODO Implement castling fully (currently only allows basic without conditions)
public class King extends OffsetPiece {
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

    /**
     * Le nombre de mouvements complétés sur la pièce
     */
    private int numberOfAppliedMoves = 0;

    public King(Colour colour) {
        super(colour);
    }

    @Override
    int getUnicodeWhite() {
        return 9812;
    }

    @Override
    int getUnicodeBlack() {
        return 9818;
    }

    @Override
    Offset[] getOffsets() {
        return OFFSETS;
    }

    @Override
    public int getUnsignedValue() {
        return 1000;
    }

    @Override
    Collection<Position> generatePossiblePositions(BoardMap board, Position start) {
        Collection<Position> positions = super.generatePossiblePositions(board, start);

        //Ajouter les options pour casteling
        Position debutTour = start.shift(new Offset(0, 3));
        Position finTour = debutTour.shift(new Offset(0, -2));
        Position finRoi = start.shift(new Offset(0, 2));

        if (board.getPiece(debutTour) instanceof Rook
                && board.getPiece(finRoi) == null
                && board.getPiece(finTour) == null
                && numberOfAppliedMoves == 0) {

            positions.add(finRoi);
        }

        return positions;
    }

    @Override
    Move makeMoveFromPosition(BoardMap board, Position start, Position end) {
        if (end.getColumn() - start.getColumn() == 2)
            return new CombineMove(start, end, new Move[]{
                    new BaseMove(end.shift(Offset.RIGHT), end.shift(Offset.LEFT))
            });

        return super.makeMoveFromPosition(board, start, end);
    }

    @Override
    public void notifyMoveComplete(Move move) {
        numberOfAppliedMoves += 1;
    }

    @Override
    public void notifyMoveUndo(Move move) {
        numberOfAppliedMoves -= 1;
    }

    @Override
    String getName() {
        return "King";
    }
}