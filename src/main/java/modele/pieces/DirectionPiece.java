package modele.pieces;

import modele.util.Couleur;
import modele.util.Offset;
import modele.util.Plateau;
import modele.util.Position;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Une pièce qui attack dans une ligne dans une direction (dame, fou et tour)
 */
abstract class DirectionPiece extends Piece {
    DirectionPiece(Couleur couleur) {
        super(couleur);
    }

    @Override
    Collection<Position> generatePosition(Plateau plateau) {
        Collection<Position> positions = new LinkedList<>();

        Position positionDebut = plateau.getPosition(this);

        //Pour chaque directions
        for (Offset direction : getDirections()) {
            Position end = positionDebut.decaler(direction);

            //Chaque fois décaler la pièce dans la direction
            while (end.isValid()) {
                Piece piece = plateau.getPiece(end);

                if (piece == null)
                    positions.add(end); //Si il n'y a rien là -> mouvement possible
                else {
                    //Si il y a une pièce d'une autre couleur on peut manger
                    if (piece.getCouleur() != couleur) positions.add(end);

                    //Une pièce bloque le chemin on ne peut pas continuer dans cette direction
                    break;
                }

                end = end.decaler(direction);
            }
        }

        return positions;
    }

    /**
     * La liste de direction possible
     */
    abstract Offset[] getDirections();
}
