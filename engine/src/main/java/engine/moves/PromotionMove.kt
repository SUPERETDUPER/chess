package engine.moves

import engine.pieces.Queen
import engine.util.Position

/**
 * A move that promotes a pawn to a queen.
 */
internal class PromotionMove(debut: Position, fin: Position) : BaseMove(debut, fin) {
    //The value is the value of the move plus the difference of the queen from pawn
    override val value: Int by lazy { super.value + Queen(piece.colour).signedValue - piece.signedValue }
}
