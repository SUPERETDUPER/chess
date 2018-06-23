package engine.pieces

import engine.moves.Move
import engine.util.*

internal class Queen(colour: Colour) : DirectionPiece(colour) {
    override val directions: Array<Position> = arrayOf(SHIFT_TOP_LEFT, SHIFT_UP, SHIFT_TOP_RIGHT, SHIFT_LEFT, SHIFT_RIGHT, SHIFT_BOTTOM_LEFT, SHIFT_DOWN, SHIFT_BOTTOM_RIGHT)

    override val unicodeWhite: Int = 9813

    override val unicodeBlack: Int = 9819

    override val unsignedValue: Int = 8

    override val name: String = "Queen"

    override fun notifyMoveComplete(move: Move) {}

    override fun notifyMoveUndo(move: Move) {}
}
