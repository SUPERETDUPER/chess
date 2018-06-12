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

    private int numberOfAppliedMoves = 0;

    private Piece promotedQueen = null;

    public Pawn(Colour colour) {
        super(colour);
    }

    @Override
    Move makeMoveFromPosition(BoardMap board, Position start, Position end) {
        if (promotedQueen != null || (end.getRow() != 0 && end.getRow() != Position.LIMIT - 1))
            return super.makeMoveFromPosition(board, start, end);

        return new PromotionMove(start, end);
    }

    @Override
    Collection<Position> generatePossiblePositions(BoardMap board, Position start) {
        if (promotedQueen != null) return promotedQueen.generatePossiblePositions(board, start);

        Collection<Position> positions = new LinkedList<>();

        boolean blocked = false;

        Position fin = start.shift(FORWARD);

        //Si une place de plus est valide est n'est pas promotion
        if (fin.isValid()) {

            //Si il y a personne on peut avancer
            if (board.getPiece(fin) == null) {
                positions.add(fin);
            }
            //Sinon on est bloqué
            else blocked = true;
        }

        //Si on est pas blocké est on a pas déjà sauté on vérifie si on peut sauter
        if (!blocked && numberOfAppliedMoves == 0) {
            fin = start.shift(FORWARD_BY_TWO);

            //Si la position et valide et la postion est vide on peut
            if (fin.isValid() && board.getPiece(fin) == null) {
                positions.add(fin);
            }
        }

        //On essaye de manger des pièces aux côtés
        fin = start.shift(RIGHT_ATTACK);
        if (canEat(board, fin)) positions.add(fin);

        fin = start.shift(LEFT_ATTACK);
        if (canEat(board, fin)) positions.add(fin);

        return positions;
    }

    private boolean canEat(BoardMap boardMap, Position fin) {
        if (fin.isValid()) {
            Piece piece = boardMap.getPiece(fin);
            return piece != null && piece.getColour() != colour;
        }

        return false;
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

        numberOfAppliedMoves += 1;
    }

    @Override
    public void notifyMoveUndo(Move move) {
        if (move instanceof PromotionMove) {
            promotedQueen = null;
        }

        numberOfAppliedMoves -= 1;
    }

    @Override
    String getName() {
        return "Pawn";
    }
}
