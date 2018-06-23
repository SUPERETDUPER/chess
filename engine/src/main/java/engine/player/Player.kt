package engine.player

import engine.GameData
import engine.moves.Move
import engine.util.Colour

import java.io.Serializable

/**
 * A player. Each implementation must decide how the player is going to play (what move it is going to play)
 */
abstract class Player : Serializable {

    /**
     * @return the players name to be show on the screen
     */
    protected abstract val name: String

    lateinit var gameData: GameData

    /**
     * Called to ask the player to submit his move via the callback method
     *
     * @param callback the consumer method through which the player should pass his move
     * @param colour   the colour of the player that should submit his move
     */
    abstract fun getMove(callback: (Move) -> Unit, colour: Colour)

    override fun toString(): String {
        return name
    }
}
