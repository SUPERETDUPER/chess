package modele.pieces;

import modele.board.Board;
import modele.board.Position;
import modele.moves.EatMove;
import modele.moves.Move;
import modele.moves.NormalMove;

import java.util.HashSet;
import java.util.Set;

abstract class RepeatOffsetPiece extends Piece {
    RepeatOffsetPiece(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public Set<Move> generateMoves(Board board) {
        Set<Move> moves = new HashSet<>();

        Position startingPosition = board.getPosition(this);

        for (int[] offset : getRepeatOffset()) {
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

    abstract int[][] getRepeatOffset();
}
