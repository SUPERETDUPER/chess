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
    public int getNumeroUnicode() {
        return colour == Colour.BLANC ? unicodeForWhite() : unicodeForBlack();
    }

    /**
     * @return la valeur de la pièce (négatif ou positif dépandant de la colour)
     */
    public int getValeur() {
        return colour == Colour.BLANC ? getValeurPositive() : -getValeurPositive();
    }

    /**
     * @return le numéro unicode de la pièce blance
     */
    abstract int unicodeForWhite();

    /**
     * @return le numéro unicode de la pièce noire
     */
    abstract int unicodeForBlack();

    abstract Collection<Position> generatePosition(BoardMap boardMap, Position positionDebut);

    Move convertir(BoardMap boardMap, Position debut, Position finale) {
        return new BaseMove(debut, finale);
    }

    public Collection<Move> generateAllMoves(BoardMap boardMap, Position positionDebut) {
        Collection<Position> positions = generatePosition(boardMap, positionDebut);
        Collection<Move> moves = new LinkedList<>();

        for (Position position : positions) {
            moves.add(convertir(boardMap, positionDebut, position));
        }

        return moves;
    }

    public boolean attaquePosition(BoardMap boardMap, Position position) {
        Collection<Position> positions = generatePosition(boardMap, boardMap.getPosition(this));
        return positions.contains(position);
    }

    /**
     * @return La valeur de la pièce indépendament de la colour
     */
    protected abstract int getValeurPositive();

    /**
     * Appelé pour signifier que le moves à été appliqué sur la pièce
     *
     * @param move le moves qui a été appliqué sur la pièce
     */
    public abstract void notifyMoveCompleted(Move move);

    /**
     * Appelé pour signifier que le moves à été appliqué sur la pièce
     *
     * @param move le moves qui a été appliqué sur la pièce
     */
    public abstract void notifyMoveUndo(Move move);

    abstract String getNom();

    @Override
    public String toString() {
        return getNom() + "-" + (colour == Colour.BLANC ? "b" : "n");
    }
}
