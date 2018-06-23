package ui.game

import com.jfoenix.controls.JFXDrawer
import com.jfoenix.controls.JFXHamburger
import com.jfoenix.controls.JFXListView
import engine.Game
import engine.Loader
import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.Alert
import javafx.scene.layout.StackPane

/**
 * Controls the game page
 *
 * @param exit   the method to run to exit the game and return to the main menu
 * @property loader the loader that loads the game
 */
class GameController(exit: () -> Unit, private val loader: Loader) {
    @FXML
    private lateinit var boardContainer: StackPane

    @FXML
    private lateinit var drawer: JFXDrawer

    @FXML
    private lateinit var drawerList: JFXListView<Action>

    @FXML
    private lateinit var hamburger: JFXHamburger

    private val boardPane: BoardPane = BoardPane(loader.game)

    //Add an action to return to the main menu
    private val actions = FXCollections.observableArrayList<Action>(Action("Return to main menu", exit))

    init {

        //Add a listener for when the game ends
        loader.game.setResultListener { result -> Platform.runLater { handleGameResult(result) } }

        //Count the number of human players
        var numberOfHumans = 0

        for (player in loader.game.players.values) {
            if (player is HumanPlayer) {
                player.attachUI(boardPane) //Attach the UI
                numberOfHumans++
            }
        }

        //If there are human players put an undo button
        if (numberOfHumans != 0)
            actions.add(Action("Undo") {
                if (numberOfHumans == 2) {
                    loader.game.undo(1) //Undo 1 turn in human vs human
                } else {
                    loader.game.undo(2) //Undo 2 turns in human vs computer
                }
            })
    }

    @FXML
    private fun initialize() {
        //Add the pieceMap to its container
        boardContainer.children.add(boardPane)

        //Add the actions to the list
        drawerList.items = actions

        //Add a click listener to the list to register when an item was pressed
        //TODO Change because throws error if called before item is selected
        drawerList.setOnMouseClicked {
            drawerList.selectionModel.selectedItem.onClick()
            drawerList.selectionModel.clearSelection()
        }

        //Animate the hamburger when the drawer opens/closes
        val animationRate = Math.abs(hamburger.animation.rate)

        drawer.setOnDrawerOpening {
            val burgerAnimation = hamburger.animation
            burgerAnimation.rate = animationRate
            burgerAnimation.play()
        }

        drawer.setOnDrawerClosing {
            val burgerAnimation = hamburger.animation
            burgerAnimation.rate = -animationRate
            burgerAnimation.play()
        }

        loader.game.notifyNextPlayer() //Start the game
    }

    @FXML
    private fun handleHamburgerClick() {
        drawer.toggle() //When the hamburger is pressed open/close the drawer
    }

    /**
     * Called when the result of the game is obtained
     */
    private fun handleGameResult(result: Game.Result) {
        //Create an alert
        //TODO Switch to material design guidelines for alert box
        val alert = Alert(Alert.AlertType.INFORMATION)
        when (result) {
            Game.Result.TIE -> alert.contentText = "Stalemate"
            Game.Result.BLACK_WINS -> alert.contentText = "Black wins"
            Game.Result.WHITE_WINS -> alert.contentText = "White wins"
        }

        alert.showAndWait()
    }

    /**
     * An action in the drawer
     */
    internal data class Action(private val name: String, val onClick: () -> Unit) {
        override fun toString(): String {
            return name //Defines the string to display when the item is put in the list view
        }
    }
}
