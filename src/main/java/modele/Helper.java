package modele;

import modele.board.Board;
import modele.moves.Move;
import modele.pieces.Piece;
import modele.pieces.Roi;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class Helper {
    public static boolean boardIsLegal(Board board, Piece roi) {
        for (Piece piece : board.iteratePieces()) {
            if (piece.canEat(roi) && piece.attacksPosition(board, board.getPosition(roi))) {
                return false;
            }
        }
        return true;
    }

    @NotNull
    public static Set<Move> getAllLegalMoves(boolean forWhite, Board board, Roi roi) {
        Set<Move> moves = new HashSet<>();

        for (Piece piece : board.iteratePieces()) {
            if (piece.isWhite() == forWhite) {
                moves.addAll(piece.getLegalMoves(board, roi));
            }
        }

        return moves;
    }
}
