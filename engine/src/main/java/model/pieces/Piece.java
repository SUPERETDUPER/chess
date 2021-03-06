package model.pieces;

import model.GameData;
import model.moves.BaseMove;
import model.moves.Move;
import model.util.BoardMap;
import model.util.Colour;
import model.util.Position;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;

/**
 * A piece in the game
 */
public abstract class Piece implements Serializable {
    final Colour colour;

    Piece(Colour colour) {
        this.colour = colour;
    }

    /**
     * @return the pieces colour (white or black)
     */
    public Colour getColour() {
        return colour;
    }

    /**
     * @return the unicode for the piece to allow it to be displayed as text
     */
    public int getUnicode() {
        return colour == Colour.WHITE ? getUnicodeWhite() : getUnicodeBlack();
    }

    /**
     * @return the value of the piece depending on its color. White is positive, black is negative
     */
    public int getSignedValue() {
        return colour == Colour.WHITE ? getUnsignedValue() : -getUnsignedValue();
    }

    /**
     * @return the unicode for the white version of the piece
     */
    abstract int getUnicodeWhite();

    /**
     * @return the unicode for the black version of the piece
     */
    abstract int getUnicodeBlack();

    /**
     * Does not check for checks (king being attacked)
     *
     * @param start where the piece is at right now
     * @return the list of positions where the piece can move
     */
    abstract Collection<Position> generatePossibleDestinations(GameData gameData, Position start);

    /**
     * Separate method to allow overriding from subclasses if a special move is required
     *
     * @return a move that will move the piece from its current position to its destination
     */
    Move convertDestinationToMove(BoardMap board, @NotNull Position current, @NotNull Position destination) {
        return new BaseMove(current, destination);
    }

    /**
     * Does not verify if move is legal (if king is put in check)
     *
     * @return a collection of all the moves that can be executed
     */
    @NotNull
    public Collection<Move> generatePossibleMoves(@NotNull GameData gameData, @NotNull Position start) {
        Collection<Move> moves = new LinkedList<>();

        //For each possible destination create a move and add it to the list
        generatePossibleDestinations(gameData, start).forEach(destination -> moves.add(convertDestinationToMove(gameData.getBoard(), start, destination)));

        return moves;
    }

    public boolean isAttackingPosition(@NotNull GameData gameData, Position position) {
        Collection<Position> positions = generatePossibleDestinations(gameData, gameData.getBoard().getPosition(this));
        return positions.contains(position);
    }

    /**
     * @return the positive value of this piece
     */
    protected abstract int getUnsignedValue();

    /**
     * Called when a move is applied to this piece
     *
     * @param move the move that was applied
     */
    public abstract void notifyMoveComplete(Move move);

    /**
     * Called when a move is undone on this piece
     *
     * @param move the move that was undone
     */
    public abstract void notifyMoveUndo(Move move);

    abstract String getName();

    @NotNull
    @Override
    public String toString() {
        return getName() + "-" + (colour == Colour.WHITE ? "w" : "b");
    }
}
