package ui.intro

import com.jfoenix.controls.JFXComboBox
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.layout.VBox
import engine.player.Player
import engine.player.PlayerComputer
import engine.util.Colour
import ui.game.HumanPlayer
import java.util.*

/**
 * Controls the main menu window
 */
class IntroController(private val onStart: (EnumMap<Colour, Player>) -> Unit) {

    @FXML
    private lateinit var whitePlayerContainer: VBox

    @FXML
    private lateinit var blackPlayerContainer: VBox

    //The drop-downs to select which player is playing
    private val whitePlayers = JFXComboBox(PLAYER_OPTIONS)
    private val blackPlayers = JFXComboBox(PLAYER_OPTIONS)

    init {

        //Select the first option
        blackPlayers.selectionModel.select(0)
        whitePlayers.selectionModel.select(0)
    }

    @FXML
    private fun initialize() {
        //Add the drop-downs to the interface
        whitePlayerContainer.children.add(whitePlayers)
        blackPlayerContainer.children.add(blackPlayers)
    }

    /**
     * Called when the start button is pressed
     */
    @FXML
    private fun handleStart() {
        //Get the 2 selected players and add them to a EnumMap
        val players = EnumMap<Colour, Player>(Colour::class.java)
        players[Colour.WHITE] = whitePlayers.selectionModel.selectedItem
        players[Colour.BLACK] = blackPlayers.selectionModel.selectedItem

        //Call the callback with the players
        onStart(players)
    }

    companion object {
        /**
         * The options for the two players
         */
        private val PLAYER_OPTIONS = FXCollections.observableArrayList(
                HumanPlayer(),
                PlayerComputer(PlayerComputer.EASY),
                PlayerComputer(PlayerComputer.HARD)
        )
    }
}