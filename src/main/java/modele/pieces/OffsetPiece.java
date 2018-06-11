package modele.pieces;

import modele.mouvement.Mouvement;
import modele.mouvement.MouvementBouger;
import modele.mouvement.MouvementManger;
import modele.util.Couleur;
import modele.util.Offset;
import modele.util.Plateau;
import modele.util.Position;

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

        //Pour chaque offset
        for (Offset offset : getOffsets()) {
            Position nextPosition = currentPose.decaler(offset);

            //Si la position n'est pas valide passer à la prochaine
            if (!nextPosition.isValid()) continue;

            Piece piece = plateau.getPiece(nextPosition);

            //si il y a une pièce de la même couleur à cette position, passer à la prochaine sinon on peut bouger
            if (piece == null) mouvements.add(new MouvementBouger(this, nextPosition));
            else if (piece.getCouleur() != couleur) mouvements.add(new MouvementManger(this, nextPosition));
        }

        return mouvements;
    }

    @Override
    public boolean attaquePosition(Plateau plateau, Position position) {
        //TODO Consider changing to subtract two positions and calculate offset then compare (using hashmap)
        Position currentPosition = plateau.getPosition(this);

        //Pour chaque offset vérifier si c'est la position
        for (Offset offset : getOffsets()) {
            if (position.equals(currentPosition.decaler(offset))) return true;
        }

        return false;
    }

    /**
     * @return les offsets où la pièce peut se décaler
     */
    abstract Offset[] getOffsets();
}
