package model.pieces;

import model.moves.BaseMove;
import model.moves.Move;
import model.util.BoardMap;
import model.util.Colour;
import model.util.Position;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Une pièce de gamewindow
 */
//TODO consider changing to decorator model for more flexibility and better implementation of special cases
public abstract class Piece implements Serializable {
    final Colour colour;

    Piece(Colour colour) {
        this.colour = colour;
    }

    /**
     * @return La colour de la pièce
     */
    public Colour getColour() {
        return colour;
    }

    /**
     * @return le numéro de la pièce en unicode
     */
    public int getUnicode() {
        return colour == Colour.WHITE ? getUnicodeWhite() : getUnicodeBlack();
    }

    /**
     * @return la valeur de la pièce (négatif ou positif dépandant de la colour)
     */
    public int getSignedValue() {
        return colour == Colour.WHITE ? getUnsignedValue() : -getUnsignedValue();
    }

    /**
     * @return le numéro unicode de la pièce blance
     */
    abstract int getUnicodeWhite();

    /**
     * @return le numéro unicode de la pièce noire
     */
    abstract int getUnicodeBlack();

    abstract Collection<Position> generatePossiblePositions(BoardMap board, Position start);

    Move makeMoveFromPosition(BoardMap board, Position start, Position end) {
        return new BaseMove(start, end);
    }

    public Collection<Move> generateAllMoves(BoardMap board, Position start) {
        Collection<Position> positions = generatePossiblePositions(board, start);
        Collection<Move> moves = new LinkedList<>();

        for (Position position : positions) {
            moves.add(makeMoveFromPosition(board, start, position));
        }

        return moves;
    }

    public boolean isAttackingPosition(BoardMap board, Position position) {
        Collection<Position> positions = generatePossiblePositions(board, board.getPosition(this));
        return positions.contains(position);
    }

    /**
     * @return La valeur de la pièce indépendament de la colour
     */
    protected abstract int getUnsignedValue();

    /**
     * Appelé pour signifier que le moves à été appliqué sur la pièce
     *
     * @param move le moves qui a été appliqué sur la pièce
     */
    public abstract void notifyMoveComplete(Move move);

    /**
     * Appelé pour signifier que le moves à été appliqué sur la pièce
     *
     * @param move le moves qui a été appliqué sur la pièce
     */
    public abstract void notifyMoveUndo(Move move);

    abstract String getName();

    @Override
    public String toString() {
        return getName() + "-" + (colour == Colour.WHITE ? "b" : "n");
    }
}
