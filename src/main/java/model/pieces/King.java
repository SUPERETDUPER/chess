package model.pieces;

import model.GameData;
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
     * The number of moves that have been applied to this piece. Used to know if the piece has moved
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

    /**
     * Large value to show that the King is the most valuable and should not be eaten
     */
    @Override
    public int getUnsignedValue() {
        return 1000;
    }

    @Override
    Collection<Position> generatePossibleDestinations(GameData gameData, Position start) {
        Collection<Position> positions = super.generatePossibleDestinations(gameData, start);

        //Add option for castling
        Position debutTour = start.shift(new Offset(0, 3));
        Position finTour = debutTour.shift(new Offset(0, -2));
        Position finRoi = start.shift(new Offset(0, 2));

        if (gameData.getBoard().getPiece(debutTour) instanceof Rook
                && gameData.getBoard().getPiece(finRoi) == null
                && gameData.getBoard().getPiece(finTour) == null
                && numberOfAppliedMoves == 0) {

            positions.add(finRoi);
        }

        return positions;
    }

    @Override
    Move convertDestinationToMove(BoardMap board, Position current, Position destination) {
        //Add catch to convert castling to CombineMove
        if (destination.getColumn() - current.getColumn() == 2)
            return new CombineMove(current, destination, new Move[]{
                    new BaseMove(destination.shift(Offset.RIGHT), destination.shift(Offset.LEFT))
            });

        return super.convertDestinationToMove(board, current, destination);
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