package engine.pieces

import engine.moves.Move
import engine.util.Colour
import engine.util.Position

internal class Knight(colour: Colour) : PositionPiece(colour) {

    override val offsets: Array<Position> = arrayOf(Position(-1, -2), Position(-2, -1), Position(-2, 1), Position(-1, 2), Position(1, 2), Position(2, 1), Position(2, -1), Position(1, -2))

    override val unicodeWhite: Int = 9816

    override val unicodeBlack: Int = 9822

    override val unsignedValue: Int = 3

    override val name: String = "Knight"

    override fun notifyMoveComplete(move: Move) {}

    override fun notifyMoveUndo(move: Move) {}
}
