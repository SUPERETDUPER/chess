package model.pieces

import model.moves.Move
import model.util.Colour
import model.util.Offset

internal class Rook(colour: Colour) : DirectionPiece(colour) {

    /**
     * The number of moves that have been applied to this piece. Used to know if the piece has moved
     */
    private var numberOfAppliedMoves = 0

    override val unicodeWhite: Int
        get() = 9814

    override val unicodeBlack: Int
        get() = 9820

    override val directions: Array<Offset>
        get() = OFFSETS

    override val unsignedValue: Int
        get() = 5

    override val name: String
        get() = "Rook"

    override fun notifyMoveComplete(move: Move) {
        numberOfAppliedMoves += 1
    }

    override fun notifyMoveUndo(move: Move) {
        numberOfAppliedMoves -= 1
    }

    internal fun hasMoved(): Boolean {
        return numberOfAppliedMoves != 0
    }

    companion object {
        private val OFFSETS = arrayOf(Offset.UP, Offset.LEFT, Offset.RIGHT, Offset.DOWN)
    }
}
