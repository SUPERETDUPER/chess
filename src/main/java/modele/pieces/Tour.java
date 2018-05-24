package modele.pieces;

import modele.board.Board;
import modele.board.Position;
import modele.moves.EatMove;
import modele.moves.Move;
import modele.moves.NormalMove;

import java.util.HashSet;
import java.util.Set;

public class Tour extends Piece {
    private static final int[][] OFFSET = {
            {-1, 0},
            {0, 1},
            {1, 0},
            {0, -1}
    };

    public Tour(boolean isWhite) {
        super(isWhite);
    }

    @Override
    int unicodeForWhite() {
        return 9814;
    }

    @Override
    int unicodeForBlack() {
        return 9820;
    }

    @Override
    public Set<Move> generateMoves(Board board) {
        Set<Move> moves = new HashSet<>();

        Position startingPosition = board.getPosition(this);

        for (int[] offset : OFFSET) {
            Position end = startingPosition.offset(offset[0], offset[1]);

            while (end.isValid()) {
                Piece piece = board.getPiece(end);

                if (piece == null) moves.add(new NormalMove(startingPosition, end));
                else if (piece.isWhite()) break;
                else {
                    moves.add(new EatMove(startingPosition, end));
                    break;
                }

                end = end.offset(offset[0], offset[1]);
            }
        }

        return moves;
    }
}
