package engine.pieces

import engine.moves.Move
import engine.util.*

internal class Rook(colour: Colour) : DirectionPiece(colour) {

    /**
     * The number of moves that have been applied to this pieceMap. Used to know if the pieceMap has moved
     */
    var numberOfAppliedMoves = 0
        private set

    override val unicodeWhite: Int = 9814

    override val unicodeBlack: Int = 9820

    override val directions: Array<Position> = arrayOf(SHIFT_UP, SHIFT_LEFT, SHIFT_RIGHT, SHIFT_DOWN)

    override val unsignedValue: Int = 5

    override val name: String = "Rook"

    override fun notifyMoveComplete(move: Move) {
        numberOfAppliedMoves += 1
    }

    override fun notifyMoveUndo(move: Move) {
        numberOfAppliedMoves -= 1
    }
}
