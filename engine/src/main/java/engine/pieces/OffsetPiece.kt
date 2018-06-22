package engine.pieces

import engine.GameData
import engine.util.Colour
import engine.util.Offset
import engine.util.Position
import java.util.*

/**
 * A piece that can move to squares next to it (King and Knight)
 */
internal abstract class OffsetPiece(colour: Colour) : Piece(colour) {

    /**
     * the offsets where the piece can move
     */
    internal abstract val offsets: Array<Offset>

    override fun generatePossibleDestinations(gameData: GameData, start: Position): MutableCollection<Position> {
        val positions = LinkedList<Position>()

        //For each possible position
        for (offset in offsets) {
            val nextPosition = start.shift(offset)

            //If not valid skip
            if (!nextPosition.isValid) continue

            val piece = gameData.board.getPiece(nextPosition)

            //If empty or piece other color can move
            if (piece == null || piece.colour != colour) positions.add(nextPosition)
        }

        return positions
    }
}
