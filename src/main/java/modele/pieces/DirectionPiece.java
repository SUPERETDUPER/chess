package modele.pieces;

import modele.board.Board;
import modele.board.Position;
import modele.moves.EatMove;
import modele.moves.Move;
import modele.moves.NormalMove;

import java.util.HashSet;
import java.util.Set;

/**
 * Une pièce qui attack dans une direction (ex. dame, fou, tour)
 */
abstract class DirectionPiece extends Piece {
    DirectionPiece(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public Set<Move> generateAllMoves(Board board) {
        Set<Move> moves = new HashSet<>();

        Position startingPosition = board.getPosition(this);

        for (int[] direction : getDirections()) {
            Position end = startingPosition.offset(direction[0], direction[1]);

            while (end.isValid()) {
                Piece piece = board.getPiece(end);

                if (piece == null) moves.add(new NormalMove(startingPosition, end));
                else {
                    if (canEat(piece)) {
                        moves.add(new EatMove(startingPosition, end));
                    }

                    break;
                }

                end = end.offset(direction[0], direction[1]);
            }
        }

        return moves;
    }

    @Override
    public boolean attacksPosition(Board board, Position position) {
        Position startingPosition = board.getPosition(this);

        for (int[] direction : getDirections()) {
            Position testPosition = startingPosition.offset(direction[0], direction[1]);

            while (testPosition.isValid()) {
                if (testPosition.equals(position)) {
                    return true;
                }

                if (board.getPiece(testPosition) != null) break;

                testPosition = testPosition.offset(direction[0], direction[1]);
            }
        }
        return false;
    }

    abstract int[][] getDirections();
}
