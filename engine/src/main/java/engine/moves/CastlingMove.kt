package engine.moves

import engine.GameData
import engine.util.Position

/**
 * A move which executes other moves at the same time
 *
 * @property rookMove the rooks move
 */
internal class CastlingMove(debut: Position, fin: Position, private val rookMove: Move) : BaseMove(debut, fin) {

    //Do not need to override value cause = 0

    override fun applyToGame(data: GameData) {
        super.applyToGame(data)

        rookMove.apply(data)
    }

    override fun undoToGame(data: GameData) {
        rookMove.undo(data)

        super.undoToGame(data)
    }
}
