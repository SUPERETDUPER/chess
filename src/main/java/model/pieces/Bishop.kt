package model.pieces

import model.moves.Move
import model.util.Colour
import model.util.Offset

internal class Bishop(colour: Colour) : DirectionPiece(colour) {

    override val directions: Array<Offset>
        get() = DIRECTIONS

    override val unicodeWhite: Int
        get() = 9815

    override val unicodeBlack: Int
        get() = 9821

    override val unsignedValue: Int
        get() = 3

    override val name: String
        get() = "Bishop"

    override fun notifyMoveComplete(move: Move) {}

    override fun notifyMoveUndo(move: Move) {}

    companion object {
        private val DIRECTIONS = arrayOf(Offset.TOP_LEFT, Offset.TOP_RIGHT, Offset.BOTTOM_LEFT, Offset.BOTTOM_RIGHT)
    }
}
