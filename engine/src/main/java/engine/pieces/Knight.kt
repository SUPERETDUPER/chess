package engine.pieces

import engine.moves.Move
import engine.util.Colour
import engine.util.Offset

internal class Knight(colour: Colour) : OffsetPiece(colour) {

    override val offsets: Array<Offset> = arrayOf(Offset(-1, -2), Offset(-2, -1), Offset(-2, 1), Offset(-1, 2), Offset(1, 2), Offset(2, 1), Offset(2, -1), Offset(1, -2))

    override val unicodeWhite: Int = 9816

    override val unicodeBlack: Int = 9822

    override val unsignedValue: Int = 3

    override val name: String = "Knight"

    override fun notifyMoveComplete(move: Move) {}

    override fun notifyMoveUndo(move: Move) {}

}
