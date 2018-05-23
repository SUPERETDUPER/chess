package modele.pieces;

import modele.board.Board;
import modele.board.Position;
import modele.moves.Move;

import java.util.HashSet;
import java.util.Set;

public class Roi extends Piece {
    private static final int[][] OFFSETS = {
            {-1, 0},
            {-1, 0},
            {-1, 1},
            {0, -1},
            {0, 1},
            {1, -1},
            {1, 0},
            {1, 1}
    };

    public Roi(boolean isWhite) {
        super(isWhite);
    }

    @Override
    int unicodeForWhite() {
        return 9812;
    }

    @Override
    int unicodeForBlack() {
        return 9818;
    }

    @Override
    public Set<Move> generateMoves(Board board) {
        Set<Move> moves = new HashSet<>();

        Position currentPose = board.getPosition(this);

        for (int[] offset : OFFSETS) {
            Position nextPosition = currentPose.offset(offset[0], offset[1]);

            //Si la position n'est pas valide passer à la prochaine
            if (!nextPosition.isValid()) continue;

            Piece pieceAPosition = board.getPiece(nextPosition);

            //si il y a une pièce de la même couleur à cette position, passer à la prochaine
            if (pieceAPosition != null && pieceAPosition.isWhite() == this.isWhite()) continue;

            moves.add(new Move(currentPose, nextPosition));
        }

        return moves;
    }
}
