package modele.pieces;

import modele.board.Board;
import modele.board.Position;
import modele.moves.EatMove;
import modele.moves.Move;
import modele.moves.NormalMove;

import java.util.HashSet;
import java.util.Set;

/**
 * Un morceau qui peut attacker les positions à ses côtés (Ex. cavalier, roi)
 */
abstract class OffsetPiece extends Piece {
    OffsetPiece(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public Set<Move> generateAllMoves(Board board) {
        Set<Move> moves = new HashSet<>();

        Position currentPose = board.getPosition(this);

        for (int[] offset : getOffsets()) {
            Position nextPosition = currentPose.offset(offset[0], offset[1]);

            //Si la position n'est pas valide passer à la prochaine
            if (!nextPosition.isValid()) continue;

            Piece piece = board.getPiece(nextPosition);

            //si il y a une pièce de la même couleur à cette position, passer à la prochaine
            if (piece == null) moves.add(new NormalMove(currentPose, nextPosition));
            else if (canEat(piece)) moves.add(new EatMove(currentPose, nextPosition));
        }

        return moves;
    }

    @Override
    public boolean attacksPosition(Board board, Position position) {
        Position currentPosition = board.getPosition(this);

        for (int[] offset : getOffsets()) {
            if (position.equals(currentPosition.offset(offset[0], offset[1]))) return true;
        }

        return false;
    }

    abstract int[][] getOffsets();
}
