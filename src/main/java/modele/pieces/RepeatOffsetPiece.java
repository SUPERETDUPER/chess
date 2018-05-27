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
    public Set<Move> generateAllMoves(Board board) {
        Set<Move> moves = new HashSet<>();

        Position startingPosition = board.getPosition(this);

        for (int[] offset : getRepeatOffset()) {
            Position end = startingPosition.offset(offset[0], offset[1]);

            while (end.isValid()) {
                Piece piece = board.getPiece(end);

                if (piece == null) moves.add(new NormalMove(startingPosition, end));
                else {
                    if (canEat(piece)) {
                        moves.add(new EatMove(startingPosition, end));
                    }

                    break;
                }

                end = end.offset(offset[0], offset[1]);
            }
        }

        return moves;
    }

    @Override
    boolean attacksPosition(Board board, Position position) {
        Position startingPosition = board.getPosition(this);

        for (int[] offset : getRepeatOffset()) {
            Position testPosition = startingPosition.offset(offset[0], offset[1]);

            while (testPosition.isValid()) {
                if (testPosition.equals(position)) {
                    return true;
                }

                if (board.getPiece(testPosition) != null) break;

                testPosition = testPosition.offset(offset[0], offset[1]);
            }
        }
        return false;
    }

    abstract int[][] getRepeatOffset();
}
