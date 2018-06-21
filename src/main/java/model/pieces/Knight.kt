package model.pieces

import model.moves.Move
import model.util.Colour
import model.util.Offset

internal class Knight(colour: Colour) : OffsetPiece(colour) {

    override val offsets: Array<Offset>
        get() = OFFSETS

    override val unicodeWhite: Int
        get() = 9816

    override val unicodeBlack: Int
        get() = 9822

    override val unsignedValue: Int
        get() = 3

    override val name: String
        get() = "Knight"

    override fun notifyMoveComplete(move: Move) {}

    override fun notifyMoveUndo(move: Move) {}

    companion object {
        /**
         * The Offsets where the Knight can be moved. In an L shape
         */
        private val OFFSETS = arrayOf(Offset(-1, -2), Offset(-2, -1), Offset(-2, 1), Offset(-1, 2), Offset(1, 2), Offset(2, 1), Offset(2, -1), Offset(1, -2))
    }
}
