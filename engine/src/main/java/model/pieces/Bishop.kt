package model.pieces

import model.moves.Move
import model.util.Colour
import model.util.Offset

internal class Bishop(colour: Colour) : DirectionPiece(colour) {

    override val directions: Array<Offset> = DIRECTIONS

    override val unicodeWhite: Int = 9815

    override val unicodeBlack: Int = 9821

    override val unsignedValue: Int = 3

    override val name: String = "Bishop"

    override fun notifyMoveComplete(move: Move) {}

    override fun notifyMoveUndo(move: Move) {}

    companion object {
        private val DIRECTIONS = arrayOf(Offset.TOP_LEFT, Offset.TOP_RIGHT, Offset.BOTTOM_LEFT, Offset.BOTTOM_RIGHT)
    }
}
