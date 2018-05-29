package modele.pieces;

import modele.moves.EatMove;
import modele.moves.Move;
import modele.moves.NormalMove;
import modele.plateau.Plateau;
import modele.plateau.Position;

import java.util.HashSet;
import java.util.Set;

/**
 * Un morceau qui peut attacker les positions à ses côtés (Ex. cavalier, roi)
 */
abstract class OffsetPiece extends Piece {
    OffsetPiece(Couleur couleur) {
        super(couleur);
    }

    @Override
    public Set<Move> generateAllMoves(Plateau plateau) {
        Set<Move> moves = new HashSet<>();

        Position currentPose = plateau.getPosition(this);

        for (int[] offset : getOffsets()) {
            Position nextPosition = currentPose.offset(offset[0], offset[1]);

            //Si la position n'est pas valide passer à la prochaine
            if (!nextPosition.isValid()) continue;

            Piece piece = plateau.getPiece(nextPosition);

            //si il y a une pièce de la même couleur à cette position, passer à la prochaine
            if (piece == null) moves.add(new NormalMove(currentPose, nextPosition));
            else if (piece.getCouleur() != couleur) moves.add(new EatMove(currentPose, nextPosition));
        }

        return moves;
    }

    @Override
    public boolean attacksPosition(Plateau plateau, Position position) {
        Position currentPosition = plateau.getPosition(this);

        for (int[] offset : getOffsets()) {
            if (position.equals(currentPosition.offset(offset[0], offset[1]))) return true;
        }

        return false;
    }

    abstract int[][] getOffsets();
}
