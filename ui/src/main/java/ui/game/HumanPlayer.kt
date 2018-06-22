package ui.game

import engine.GameData
import engine.moves.Move
import engine.player.Player
import engine.util.Colour

/**
 * A player that uses the UI to submit his moves
 */
class HumanPlayer : Player() {
    /**
     * The UI
     */
    @Transient
    private var boardPane: BoardPane? = null

    override val name: String
        get() = "Human"

    override fun initializeGameData(gameData: GameData) {

    }

    internal fun attachUI(boardPane: BoardPane) {
        this.boardPane = boardPane
    }

    /**
     * Requests the UI (BoardPane) to allow the user to submit his move
     *
     * @param callback the callback method where the selected move should eventually be submitted
     * @param colour   the colour of the player that should submit his move
     */
    override fun getMove(callback: (Move)->Unit, colour: Colour) {
        boardPane!!.requestMove(callback, colour) //Create a request and submit to the UI
    }
}
