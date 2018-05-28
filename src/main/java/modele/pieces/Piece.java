package modele.pieces;

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

    public Set<Move> generateLegalMoves(Board board, Roi roi) {
        Set<Move> moves = generateAllMoves(board);
        Set<Move> legalMoves = new HashSet<>();

        Board tempBoard = board.getCopie();

        for (Move move : moves) {
            move.apply(tempBoard);

            if (boardIsLegal(tempBoard, tempBoard.getPosition(roi))) {
                legalMoves.add(move);
            }

            move.undo(tempBoard);
        }

        return legalMoves;
    }

    private boolean boardIsLegal(Board board, Position positionDuRoi) {
        for (Piece piece : board.iteratePieces()) {
            if (canEat(piece)) {

                if (piece.attacksPosition(board, positionDuRoi)) {
                    return false;
                }
            }
        }
        return true;
    }

    public abstract Set<Move> generateAllMoves(Board board);

    abstract boolean attacksPosition(Board board, Position position);

    boolean canEat(Piece piece) {
        return piece.isWhite() != this.isWhite();
    }
}
