package model.moves

import model.GameData
import model.util.Position

/**
 * A move which executes other moves at the same time
 */
internal class CastlingMove
/**
 * @param otherMoves a list of other moves to apply with this move
 */
(debut: Position, fin: Position, private val otherMoves: Array<Move>) : BaseMove(debut, fin) {
    override val value: Int
        get() {
            var tempValue: Int = super.value

            for (move in otherMoves) {
                tempValue += move.value
            }

            return tempValue
        }


    override fun applyToGame(data: GameData) {
        super.applyToGame(data)

        for (move in otherMoves) {
            move.apply(data)
        }
    }

    override fun undoToGame(data: GameData) {
        for (i in otherMoves.size - 1 downTo 0) {
            otherMoves[i].undo(data)
        }

        super.undoToGame(data)
    }
}
