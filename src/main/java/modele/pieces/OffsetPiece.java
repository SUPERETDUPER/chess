package modele.pieces;

import modele.Couleur;
import modele.moves.Mouvement;
import modele.moves.MouvementManger;
import modele.moves.MouvementNormal;
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
    public Set<Mouvement> generateAllMoves(Plateau plateau) {
        Set<Mouvement> mouvements = new HashSet<>();

        Position currentPose = plateau.getPosition(this);

        for (Offset offset : getOffsets()) {
            Position nextPosition = currentPose.decaler(offset);

            //Si la position n'est pas valide passer à la prochaine
            if (!nextPosition.isValid()) continue;

            Piece piece = plateau.getPiece(nextPosition);

            //si il y a une pièce de la même couleur à cette position, passer à la prochaine
            if (piece == null) mouvements.add(new MouvementNormal(this, nextPosition));
            else if (piece.getCouleur() != couleur) mouvements.add(new MouvementManger(this, nextPosition));
        }

        return mouvements;
    }

    @Override
    public boolean attaquePosition(Plateau plateau, Position position) {
        Position currentPosition = plateau.getPosition(this);

        for (Offset offset : getOffsets()) {
            if (position.equals(currentPosition.decaler(offset))) return true;
        }

        return false;
    }

    abstract Offset[] getOffsets();
}
