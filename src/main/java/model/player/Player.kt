package model.player

import model.GameData
import model.moves.Move
import model.util.Colour

import java.io.Serializable
import java.util.function.Consumer

/**
 * A player. Each implementation must decide how the player is going to play (what move it is going to play)
 */
abstract class Player : Serializable {

    /**
     * @return the players name to be show on the screen
     */
    protected abstract val name: String

    /**
     * Called to ask the player to submit his move via the callback method
     *
     * @param callback the consumer method through which the player should pass his move
     * @param colour   the colour of the player that should submit his move
     */
    internal abstract fun getMove(callback: Consumer<Move>, colour: Colour)

    /**
     * @param gameData called when the player is added to the game to give it access to the game data
     */
    abstract fun initializeGameData(gameData: GameData)

    override fun toString(): String {
        return name
    }
}
