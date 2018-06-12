package modele.pieces;

import modele.util.Couleur;
import modele.util.Offset;
import modele.util.Plateau;
import modele.util.Position;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Un morceau qui peut attacker les positions à ses côtés (Ex. cavalier, roi)
 */
abstract class OffsetPiece extends Piece {
    OffsetPiece(Couleur couleur) {
        super(couleur);
    }

    @Override
    Collection<Position> generatePosition(Plateau plateau) {
        Collection<Position> positions = new LinkedList<>();

        Position positionDebut = plateau.getPosition(this);

        //Pour chaque directions
        for (Offset offset : getOffsets()) {
            Position nextPosition = positionDebut.decaler(offset);

            //Si la position n'est pas valide passer à la prochaine
            if (!nextPosition.isValid()) continue;

            Piece piece = plateau.getPiece(nextPosition);

            //si il y a une pièce de la même couleur à cette position, passer à la prochaine sinon on peut bouger
            if (piece == null || piece.getCouleur() != couleur) positions.add(nextPosition);
        }

        return positions;
    }

    /**
     * @return les offsets où la pièce peut se décaler
     */
    abstract Offset[] getOffsets();
}
