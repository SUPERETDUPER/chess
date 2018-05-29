package modele.pieces;

import modele.moves.MouvementManger;
import modele.moves.MouvementNormal;
import modele.moves.Move;
import modele.plateau.Offset;
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

        for (Offset offset : getOffsets()) {
            Position nextPosition = currentPose.offset(offset);

            //Si la position n'est pas valide passer à la prochaine
            if (!nextPosition.isValid()) continue;

            Piece piece = plateau.getPiece(nextPosition);

            //si il y a une pièce de la même couleur à cette position, passer à la prochaine
            if (piece == null) moves.add(new MouvementNormal(currentPose, nextPosition));
            else if (piece.getCouleur() != couleur) moves.add(new MouvementManger(currentPose, nextPosition));
        }

        return moves;
    }

    @Override
    public boolean attacksPosition(Plateau plateau, Position position) {
        Position currentPosition = plateau.getPosition(this);

        for (Offset offset : getOffsets()) {
            if (position.equals(currentPosition.offset(offset))) return true;
        }

        return false;
    }

    abstract Offset[] getOffsets();
}
