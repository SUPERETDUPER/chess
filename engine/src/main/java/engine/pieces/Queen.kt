package engine.pieces

import engine.moves.Move
import engine.util.Colour
import engine.util.Offset

internal class Queen(colour: Colour) : DirectionPiece(colour) {
    override val directions: Array<Offset> = arrayOf(Offset.TOP_LEFT, Offset.UP, Offset.TOP_RIGHT, Offset.LEFT, Offset.RIGHT, Offset.BOTTOM_LEFT, Offset.DOWN, Offset.BOTTOM_RIGHT)

    override val unicodeWhite: Int = 9813

    override val unicodeBlack: Int = 9819

    override val unsignedValue: Int = 8

    override val name: String = "Queen"

    override fun notifyMoveComplete(move: Move) {}

    override fun notifyMoveUndo(move: Move) {}
}
