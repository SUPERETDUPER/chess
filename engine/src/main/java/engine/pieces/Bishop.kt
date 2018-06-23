package engine.pieces

import engine.moves.Move
import engine.util.*

internal class Bishop(colour: Colour) : DirectionPiece(colour) {
    override val directions: Array<Position> = arrayOf(SHIFT_TOP_LEFT, SHIFT_TOP_RIGHT, SHIFT_BOTTOM_LEFT, SHIFT_BOTTOM_RIGHT)

    override val unicodeWhite: Int = 9815

    override val unicodeBlack: Int = 9821

    override val unsignedValue: Int = 3

    override val name: String = "Bishop"

    override fun notifyMoveComplete(move: Move) {}

    override fun notifyMoveUndo(move: Move) {}
}
