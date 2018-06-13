package model.pieces;

import model.moves.Move;
import model.moves.PromotionMove;
import model.util.BoardMap;
import model.util.Colour;
import model.util.Offset;
import model.util.Position;

import java.util.Collection;
import java.util.LinkedList;

//TODO Implement promotion and en passant
public class Pawn extends Piece {
    private final Offset RIGHT_ATTACK = getColour() == Colour.WHITE ? Offset.TOP_LEFT : Offset.BOTTOM_LEFT;
    private final Offset LEFT_ATTACK = getColour() == Colour.WHITE ? Offset.TOP_RIGHT : Offset.BOTTOM_RIGHT;
    private final Offset FORWARD = getColour() == Colour.WHITE ? Offset.UP : Offset.DOWN;
    private final Offset FORWARD_BY_TWO = new Offset(Colour.WHITE == getColour() ? -2 : 2, 0);
    private final int startRow = getColour() == Colour.WHITE ? Position.LIMIT - 2 : 1;

    /**
     * null when not promoted and not-null when pawn was promoted
     */
    private Piece promotedQueen = null;

    public Pawn(Colour colour) {
        super(colour);
    }

    @Override
    Move convertDestinationToMove(BoardMap board, Position current, Position destination) {
        //Add catch for when pawn gets promoted
        if (promotedQueen == null && (destination.getRow() == 0 || destination.getRow() == Position.LIMIT - 1))
            return new PromotionMove(current, destination);

        return super.convertDestinationToMove(board, current, destination);

    }

    @Override
    Collection<Position> generatePossibleDestinations(BoardMap board, Position start) {
        //If promoted use queen to generate moves
        if (promotedQueen != null) return promotedQueen.generatePossibleDestinations(board, start);

        Collection<Position> positions = new LinkedList<>();

        Position forward = start.shift(FORWARD);

        //If no one in spot in front can move else we are blocked
        //No need for .isValid check since when on edge piece is promoted to queen
        boolean notBlocked = board.getPiece(forward) == null;
        if (notBlocked) positions.add(forward);

        //If not blocked and on start row we can move forward by two
        //No need for .isValid check since when on edge piece is promoted to queen
        if (notBlocked && start.getRow() == startRow) {
            forward = start.shift(FORWARD_BY_TWO);

            //Only move forward by two if square is empty
            if (board.getPiece(forward) == null) positions.add(forward);
        }

        //Try to eat pieces on the side
        forward = start.shift(RIGHT_ATTACK);
        if (canEat(board, forward)) positions.add(forward);

        forward = start.shift(LEFT_ATTACK);
        if (canEat(board, forward)) positions.add(forward);

        return positions;
    }

    private boolean canEat(BoardMap boardMap, Position destination) {
        if (!destination.isValid()) return false;

        Piece piece = boardMap.getPiece(destination);
        return piece != null && piece.getColour() != colour; //Can eat if piece exists and is other colour

    }

    @Override
    int getUnicodeBlack() {
        if (promotedQueen != null) return promotedQueen.getUnicodeBlack();

        return 9823;
    }

    @Override
    int getUnicodeWhite() {
        if (promotedQueen != null) return promotedQueen.getUnicodeWhite();
        return 9817;
    }

    @Override
    public int getUnsignedValue() {
        if (promotedQueen != null) return promotedQueen.getUnsignedValue();

        return 1;
    }

    @Override
    public void notifyMoveComplete(Move move) {
        if (move instanceof PromotionMove) {
            //Make the queen equal this since when the queen searches in the data for its position it should find itself
            //TODO might not be required since position is passed in
            promotedQueen = new Queen(colour) {
                @Override
                public int hashCode() {
                    return Pawn.this.hashCode();
                }

                @Override
                public boolean equals(Object obj) {
                    return Pawn.this.equals(obj);
                }
            };
        }
    }

    @Override
    public void notifyMoveUndo(Move move) {
        if (move instanceof PromotionMove) {
            promotedQueen = null;
        }
    }

    @Override
    String getName() {
        return "Pawn";
    }
}
