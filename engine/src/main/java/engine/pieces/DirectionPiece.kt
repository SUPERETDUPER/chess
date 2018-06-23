package engine.pieces

import engine.GameData
import engine.util.Colour
import engine.util.Position
import java.util.*

/**
 * A pieceMap that attacks in a line a (or several) directions (ex. Queen, Rook, Bishop)
 */
internal abstract class DirectionPiece(colour: Colour) : Piece(colour) {

    /**
     * the list of directions the pieceMap can attack
     */
    internal abstract val directions: Array<Position>

    override fun generatePossibleDestinations(gameData: GameData, start: Position): Collection<Position> {
        //WARNING if parameter start is removed pawn promoted to queen might not work since queen will not recognize itself on the pieceMap
        val positions = LinkedList<Position>()

        //For each direction
        for (direction in directions) {
            var end = start + direction

            //Loop through all the positions in that line/direction
            while (end.isValid) {
                val piece = gameData.pieceMap[end]

                if (piece == null) positions.add(end) //If the square (position) is empty -> can move
                else {
                    //If pieceMap is other colour can eat
                    if (piece.colour != this.colour) positions.add(end)

                    //Cannot go past a pieceMap since it is blocking the line
                    break
                }

                end += direction
            }
        }

        return positions
    }
}
