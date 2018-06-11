package modele.pieces;

import modele.mouvement.Mouvement;
import modele.mouvement.MouvementManger;
import modele.mouvement.MouvementNormal;
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

        Position startingPosition = plateau.getPosition(this);

        for (Offset direction : getDirections()) {
            Position end = startingPosition.decaler(direction);

            while (end.isValid()) {
                Piece piece = plateau.getPiece(end);

                if (piece == null) mouvements.add(new MouvementNormal(this, end));
                else {
                    if (piece.getCouleur() != couleur) {
                        mouvements.add(new MouvementManger(this, end));
                    }

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

        for (Offset direction : getDirections()) {
            Position testPosition = startingPosition.decaler(direction);

            while (testPosition.isValid()) {
                if (testPosition.equals(position)) {
                    return true;
                }

                if (plateau.getPiece(testPosition) != null) break;

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
