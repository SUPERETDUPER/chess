package model.pieces

import model.moves.Move
import model.util.Colour
import model.util.Offset

internal class Queen(colour: Colour) : DirectionPiece(colour) {

    override val directions: Array<Offset>
        get() = OFFSETS

    override val unicodeWhite: Int
        get() = 9813

    override val unicodeBlack: Int
        get() = 9819

    override val unsignedValue: Int
        get() = 8

    override val name: String
        get() = "Queen"

    override fun notifyMoveComplete(move: Move) {}

    override fun notifyMoveUndo(move: Move) {}

    companion object {
        private val OFFSETS = arrayOf(Offset.TOP_LEFT, Offset.UP, Offset.TOP_RIGHT, Offset.LEFT, Offset.RIGHT, Offset.BOTTOM_LEFT, Offset.DOWN, Offset.BOTTOM_RIGHT)
    }
}
