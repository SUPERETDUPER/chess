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
 * Une pièce qui attack dans une ligne dans une direction (dame, fou et tour)
 */
abstract class DirectionPiece extends Piece {
    DirectionPiece(Couleur couleur) {
        super(couleur);
    }

    @Override
    public Set<Move> generateAllMoves(Plateau plateau) {
        Set<Move> moves = new HashSet<>();

        Position startingPosition = plateau.getPosition(this);

        for (Offset direction : getDirections()) {
            Position end = startingPosition.offset(direction);

            while (end.isValid()) {
                Piece piece = plateau.getPiece(end);

                if (piece == null) moves.add(new MouvementNormal(startingPosition, end));
                else {
                    if (piece.getCouleur() != couleur) {
                        moves.add(new MouvementManger(startingPosition, end));
                    }

                    break;
                }

                end = end.offset(direction);
            }
        }

        return moves;
    }

    @Override
    public boolean attacksPosition(Plateau plateau, Position position) {
        Position startingPosition = plateau.getPosition(this);

        for (Offset direction : getDirections()) {
            Position testPosition = startingPosition.offset(direction);

            while (testPosition.isValid()) {
                if (testPosition.equals(position)) {
                    return true;
                }

                if (plateau.getPiece(testPosition) != null) break;

                testPosition = testPosition.offset(direction);
            }
        }
        return false;
    }

    /**
     * La liste de direction possible
     */
    abstract Offset[] getDirections();
}
