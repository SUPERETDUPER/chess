package modele.pieces;

import modele.board.Board;
import modele.board.Position;
import modele.moves.Move;

import java.util.HashSet;
import java.util.Set;

public abstract class Piece {
    private final boolean isWhite;

    Piece(boolean isWhite) {
        this.isWhite = isWhite;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public String getUnicode() {
        return Character.toString((char) (isWhite ? unicodeForWhite() : unicodeForBlack()));
    }

    abstract int unicodeForWhite();

    abstract int unicodeForBlack();

    public Set<Move> generateLegalMoves(Board board, Position positionOfKing) {
        Set<Move> moves = generateAllMoves(board);
        Set<Move> legalMoves = new HashSet<>();

        for (Move move : moves) {
            System.out.println("Scanning moves: " + move);

            move.apply(board);

            if (boardIsLegal(board, positionOfKing)) {
                legalMoves.add(move);
            }

            move.undo(board);
        }

        System.out.println(legalMoves + " " + legalMoves.size());

        return legalMoves;
    }

    private boolean boardIsLegal(Board board, Position positionOfKing) {
        for (Piece piece : board.iteratePieces()) {
            if (canEat(piece)) {
                System.out.println("scanning piece: " + piece.getClass().getSimpleName());

                if (piece.attacksPosition(board, positionOfKing)) {
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
