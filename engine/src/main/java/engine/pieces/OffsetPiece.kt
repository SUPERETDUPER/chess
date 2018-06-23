package engine.pieces

import engine.GameData
import engine.util.Colour
import engine.util.Position
import java.util.*

/**
 * A pieceMap that can move to squares next to it (King and Knight)
 */
internal abstract class PositionPiece(colour: Colour) : Piece(colour) {

    /**
     * the offsets where the pieceMap can move
     */
    internal abstract val offsets: Array<Position>

    override fun generatePossibleDestinations(gameData: GameData, start: Position): MutableCollection<Position> {
        val positions = LinkedList<Position>()

        //For each possible position
        for (Position in offsets) {
            val nextPosition = start + Position

            //If not valid skip
            if (!nextPosition.isValid) continue

            val piece = gameData.pieceMap[nextPosition]

            //If empty or pieceMap other color can move
            if (piece == null || piece.colour != colour) positions.add(nextPosition)
        }

        return positions
    }
}
