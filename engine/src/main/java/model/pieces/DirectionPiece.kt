package model.pieces

import model.GameData
import model.util.Colour
import model.util.Offset
import model.util.Position
import java.util.*

/**
 * A piece that attacks in a line a (or several) directions (ex. Queen, Rook, Bishop)
 */
internal abstract class DirectionPiece(colour: Colour) : Piece(colour) {

    /**
     * the list of directions the piece can attack
     */
    internal abstract val directions: Array<Offset>

    override fun generatePossibleDestinations(gameData: GameData, start: Position): Collection<Position> {
        //WARNING if parameter start is removed pawn promoted to queen might not work since queen will not recognize itself on the board
        val positions = LinkedList<Position>()

        //For each direction
        for (direction in directions) {
            var end = start.shift(direction)

            //Loop through all the positions in that line/direction
            while (end.isValid) {
                val piece = gameData.board.getPiece(end)

                if (piece == null)
                    positions.add(end) //If the square (position) is empty -> can move
                else {
                    //If piece is other colour can eat
                    if (piece.colour != colour) positions.add(end)

                    //Cannot go past a piece since it is blocking the line
                    break
                }

                end = end.shift(direction)
            }
        }

        return positions
    }
}
