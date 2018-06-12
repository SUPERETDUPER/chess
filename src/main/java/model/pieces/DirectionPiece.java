package model.pieces;

import model.util.BoardMap;
import model.util.Colour;
import model.util.Offset;
import model.util.Position;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Une pièce qui attack dans une ligne dans une direction (dame, fou et tour)
 */
abstract class DirectionPiece extends Piece {
    DirectionPiece(Colour colour) {
        super(colour);
    }

    @Override
    Collection<Position> generatePossiblePositions(BoardMap board, Position start) {
        Collection<Position> positions = new LinkedList<>();

        //Pour chaque directions
        for (Offset direction : getDirections()) {
            Position end = start.shift(direction);

            //Chaque fois décaler la pièce dans la direction
            while (end.isValid()) {
                Piece piece = board.getPiece(end);

                if (piece == null)
                    positions.add(end); //Si il n'y a rien là -> moves possible
                else {
                    //Si il y a une pièce d'une autre colour on peut manger
                    if (piece.getColour() != colour) positions.add(end);

                    //Une pièce bloque le chemin on ne peut pas continuer dans cette direction
                    break;
                }

                end = end.shift(direction);
            }
        }

        return positions;
    }

    /**
     * La liste de direction possible
     */
    abstract Offset[] getDirections();
}
