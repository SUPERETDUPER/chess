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
 * Une pièce qui attack dans une ligne dans une direction (dame, fou et tour)
 */
abstract class DirectionPiece extends Piece {
    DirectionPiece(Couleur couleur) {
        super(couleur);
    }

    @Override
    public Set<Mouvement> generateAllMoves(Plateau plateau) {
        Set<Mouvement> mouvements = new HashSet<>();

        Position positionDebut = plateau.getPosition(this);

        //Pour chaque direction
        for (Offset direction : getDirections()) {
            Position end = positionDebut.decaler(direction);

            //Chaque fois décaler la pièce dans la direction
            while (end.isValid()) {
                Piece piece = plateau.getPiece(end);

                if (piece == null)
                    mouvements.add(new MouvementBouger(this, end)); //Si il n'y a rien là -> mouvement possible
                else {
                    //Si il y a une pièce d'une autre couleur on peut manger
                    if (piece.getCouleur() != couleur) mouvements.add(new MouvementManger(this, end));

                    //Une pièce bloque le chemin on ne peut pas continuer dans cette direction
                    break;
                }

                end = end.decaler(direction);
            }
        }

        return mouvements;
    }

    @Override
    public boolean attaquePosition(Plateau plateau, Position position) {
        Position startingPosition = plateau.getPosition(this);

        //Pour chaque direction
        for (Offset direction : getDirections()) {
            //Décaler pendant que c'est possible
            Position testPosition = startingPosition.decaler(direction);

            while (testPosition.isValid()) {
                Piece piece = plateau.getPiece(testPosition);

                if (piece == null) {
                    if (testPosition.equals(position)) return true;
                } else {
                    //Si il y a une pièce d'une autre couleur on peut manger
                    if (piece.getCouleur() != couleur && testPosition.equals(position)) return true;

                    //Une pièce bloque le chemin on ne peut pas continuer dans cette direction
                    break;
                }

                testPosition = testPosition.decaler(direction);
            }
        }

        return false;
    }

    /**
     * La liste de direction possible
     */
    abstract Offset[] getDirections();
}
