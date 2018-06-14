package model.pieces;

import model.GameData;
import model.moves.EnPassantMove;
import model.moves.Move;
import model.moves.PromotionMove;
import model.util.BoardMap;
import model.util.Colour;
import model.util.Offset;
import model.util.Position;

import java.util.Collection;
import java.util.LinkedList;

public class Pawn extends Piece {
    private final Offset RIGHT_ATTACK = getColour() == Colour.WHITE ? Offset.TOP_LEFT : Offset.BOTTOM_LEFT;
    private final Offset LEFT_ATTACK = getColour() == Colour.WHITE ? Offset.TOP_RIGHT : Offset.BOTTOM_RIGHT;
    private final Offset FORWARD = getColour() == Colour.WHITE ? Offset.UP : Offset.DOWN;
    public final Offset BACKWARD = getColour() == Colour.WHITE ? Offset.DOWN : Offset.UP;
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
        if (promotedQueen != null) return promotedQueen.convertDestinationToMove(board, current, destination);

        //Check for promotion if on last row
        if (destination.getRow() == 0 || destination.getRow() == Position.LIMIT - 1)
            return new PromotionMove(current, destination);
        //If moved sideways and there is no piece at destination must be en passant
        if (current.getColumn() != destination.getColumn() && board.getPiece(destination) == null)
            return new EnPassantMove(current, destination);

        return super.convertDestinationToMove(board, current, destination);
    }

    @Override
    Collection<Position> generatePossibleDestinations(GameData gameData, Position start) {
        //If promoted use queen to generate moves
        if (promotedQueen != null) return promotedQueen.generatePossibleDestinations(gameData, start);

        Collection<Position> positions = new LinkedList<>();

        Position forward = start.shift(FORWARD);

        //If no one in spot in front can move else we are blocked
        //No need for .isValid check since when on edge piece is promoted to queen
        boolean notBlocked = gameData.getBoard().getPiece(forward) == null;
        if (notBlocked) positions.add(forward);

        //If not blocked and on start row we can move forward by two
        //No need for .isValid check since when on edge piece is promoted to queen
        if (notBlocked && start.getRow() == startRow) {
            forward = start.shift(FORWARD_BY_TWO);

            //Only move forward by two if square is empty
            if (gameData.getBoard().getPiece(forward) == null) positions.add(forward);
        }

        //Try to eat pieces on the side (add en passant)
        forward = start.shift(RIGHT_ATTACK);
        if (canAttack(gameData, forward)) positions.add(forward);

        forward = start.shift(LEFT_ATTACK);
        if (canAttack(gameData, forward)) positions.add(forward);

        return positions;
    }

    private boolean canAttack(GameData gameData, Position destination) {
        if (!destination.isValid()) return false;

        //If piece there return true if same color
        Piece piece = gameData.getBoard().getPiece(destination);
        if (piece != null) {
            return piece.getColour() != colour;//Can eat if piece exists and is other colour
        }

        //Get piece on the side
        Piece pieceOnSide = gameData.getBoard().getPiece(destination.shift(BACKWARD));
        //If piece is null or not a pawn do nothing (or if same colour as me
        if (!(pieceOnSide instanceof Pawn) || pieceOnSide.getColour() == colour) return false;

        //Get last move
        Move lastMove = gameData.getPastMoves().getLast();

        //If the last move affected the piece on the side and the piece on the side moved by two
        return lastMove.getPiece() == pieceOnSide && Math.abs(lastMove.getStart().getRow() - lastMove.getEnd().getRow()) == 2;
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
            promotedQueen = new Queen(colour);
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
