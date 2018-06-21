package model.moves

import model.pieces.Queen
import model.util.Position

/**
 * A move that promotes a pawn to a queen.
 */
internal class PromotionMove(debut: Position, fin: Position) : BaseMove(debut, fin) {

    override val value: Int
        get() = super.value + Queen(piece!!.colour).signedValue - piece!!.signedValue
}
