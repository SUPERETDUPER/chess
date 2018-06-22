package model.pieces;

import model.GameData;
import model.util.Colour;
import model.util.Offset;
import model.util.Position;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.LinkedList;

/**
 * A piece that attacks in a line a (or several) directions (ex. Queen, Rook, Bishop)
 */
abstract class DirectionPiece extends Piece {
    DirectionPiece(Colour colour) {
        super(colour);
    }

    @NotNull
    @Override
    Collection<Position> generatePossibleDestinations(@NotNull GameData gameData, @NotNull Position start) {
        //WARNING if parameter start is removed pawn promoted to queen might not work since queen will not recognize itself on the board
        Collection<Position> positions = new LinkedList<>();

        //For each direction
        for (Offset direction : getDirections()) {
            Position end = start.shift(direction);

            //Loop through all the positions in that line/direction
            while (end.isValid()) {
                Piece piece = gameData.getBoard().getPiece(end);

                if (piece == null) positions.add(end); //If the square (position) is empty -> can move
                else {
                    //If piece is other colour can eat
                    if (piece.getColour() != colour) positions.add(end);

                    //Cannot go past a piece since it is blocking the line
                    break;
                }

                end = end.shift(direction);
            }
        }

        return positions;
    }

    /**
     * @return the list of directions the piece can attack
     */
    @NotNull
    abstract Offset[] getDirections();
}
