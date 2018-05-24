package modele;

import modele.board.Board;
import modele.moves.Move;
import modele.pieces.Piece;

import java.util.ArrayList;
import java.util.List;

public class MoveGenerator {
    private final Board board;

    public MoveGenerator(Board board) {
        this.board = board;
    }

    public List<Move> generate(boolean mustBeLegal) {
        List<Move> moves = new ArrayList<>();

        for (Piece piece : board.iteratePieces()) {
            moves.addAll(piece.generateAllMoves(board));
        }

        if (mustBeLegal) {
            //TODO remove moves that are not legal
        }

        return moves;
    }
}