package modele.pieces;

import modele.Helper;
import modele.board.Board;
import modele.board.Position;
import modele.moves.Move;

import java.util.HashSet;
import java.util.Set;

/**
 * Une pièce de jeu
 */
public abstract class Piece {
    private final boolean isWhite;

    Piece(boolean isWhite) {
        this.isWhite = isWhite;
    }

    public boolean isWhite() {
        return isWhite;
    }

    /**
     * @return le code de la pièce en unicode
     */
    public String getUnicode() {
        return Character.toString((char) (isWhite ? unicodeForWhite() : unicodeForBlack()));
    }

    abstract int unicodeForWhite();

    abstract int unicodeForBlack();

    public Set<Move> getLegalMoves(Board board, Roi roi) {
        Set<Move> moves = generateAllMoves(board);
        Set<Move> legalMoves = new HashSet<>();

        Board tempBoard = board.getCopie();

        for (Move move : moves) {
            move.apply(tempBoard);

            if (Helper.boardIsLegal(tempBoard, tempBoard.getPosition(roi))) {
                legalMoves.add(move);
            }

            move.undo(tempBoard);
        }

        return legalMoves;
    }

    public abstract Set<Move> generateAllMoves(Board board);

    public abstract boolean attacksPosition(Board board, Position position);

    public boolean canEat(Piece piece) {
        return piece.isWhite() != this.isWhite();
    }
}
