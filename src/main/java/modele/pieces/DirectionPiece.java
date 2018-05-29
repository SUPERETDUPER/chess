package modele.pieces;

import modele.moves.EatMove;
import modele.moves.Move;
import modele.moves.NormalMove;
import modele.plateau.Plateau;
import modele.plateau.Position;

import java.util.HashSet;
import java.util.Set;

/**
 * Une pièce qui attack dans une direction (ex. dame, fou, tour)
 */
abstract class DirectionPiece extends Piece {
    DirectionPiece(Couleur couleur) {
        super(couleur);
    }

    @Override
    public Set<Move> generateAllMoves(Plateau plateau) {
        Set<Move> moves = new HashSet<>();

        Position startingPosition = plateau.getPosition(this);

        for (int[] direction : getDirections()) {
            Position end = startingPosition.offset(direction[0], direction[1]);

            while (end.isValid()) {
                Piece piece = plateau.getPiece(end);

                if (piece == null) moves.add(new NormalMove(startingPosition, end));
                else {
                    if (piece.getCouleur() != couleur) {
                        moves.add(new EatMove(startingPosition, end));
                    }

                    break;
                }

                end = end.offset(direction[0], direction[1]);
            }
        }

        return moves;
    }

    @Override
    public boolean attacksPosition(Plateau plateau, Position position) {
        Position startingPosition = plateau.getPosition(this);

        for (int[] direction : getDirections()) {
            Position testPosition = startingPosition.offset(direction[0], direction[1]);

            while (testPosition.isValid()) {
                if (testPosition.equals(position)) {
                    return true;
                }

                if (plateau.getPiece(testPosition) != null) break;

                testPosition = testPosition.offset(direction[0], direction[1]);
            }
        }
        return false;
    }

    abstract int[][] getDirections();
}
