package modele.pieces;

import modele.board.Board;
import modele.board.Position;
import modele.moves.EatMove;
import modele.moves.Move;
import modele.moves.NormalMove;

import java.util.HashSet;
import java.util.Set;

abstract class OffsetPiece extends Piece {
    OffsetPiece(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public Set<Move> generateMoves(Board board) {
        Set<Move> moves = new HashSet<>();

        Position currentPose = board.getPosition(this);

        for (int[] offset : getOffsets()) {
            Position nextPosition = currentPose.offset(offset[0], offset[1]);

            //Si la position n'est pas valide passer à la prochaine
            if (!nextPosition.isValid()) continue;

            Piece pieceAPosition = board.getPiece(nextPosition);

            //si il y a une pièce de la même couleur à cette position, passer à la prochaine
            if (pieceAPosition == null) moves.add(new NormalMove(currentPose, nextPosition));
            else if (pieceAPosition.isWhite() != this.isWhite()) moves.add(new EatMove(currentPose, nextPosition));
        }

        return moves;
    }

    abstract int[][] getOffsets();
}
