package ui.game

import com.jfoenix.controls.JFXDrawer
import com.jfoenix.controls.JFXHamburger
import com.jfoenix.controls.JFXListView
import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.Alert
import javafx.scene.layout.StackPane
import engine.Game
import engine.Loader

/**
 * Controls the game page
 */
class GameController
/**
 * @param exit   the method to run to exit the game and return to the main menu
 * @param loader the loader that loads the game
 */
(exit: () -> Unit,
 /**
  * Loads the engine
  */
 private val loader: Loader) {
    @FXML
    private var boardContainer: StackPane? = null

    @FXML
    private var drawer: JFXDrawer? = null

    @FXML
    private var drawerList: JFXListView<Action>? = null

    @FXML
    private var hamburger: JFXHamburger? = null

    private val boardPane: BoardPane = BoardPane(loader.game!!)

    private val actions = FXCollections.observableArrayList<Action>()

    init {

        //Add a listener for when the game ends
        loader.game!!.setResultListener { result -> Platform.runLater { handleGameResult(result) } }

        //Count the number of human players
        var counter = 0

        for (player in loader.game!!.players.values) {
            if (player is HumanPlayer) {
                player.attachUI(boardPane) //Attach the UI
                counter += 1
            }
        }

        val numberOfHumans = counter

        //Add an action to return to the main menu
        actions.add(object : Action() {

            override val name: String
                get() = "Return to main menu"

            override fun onClick() {
                exit.invoke()
            }
        })

        //If there are human players add an undo button
        if (numberOfHumans != 0) {
            actions.add(object : Action() {

                override val name: String
                    get() = "Undo"

                override fun onClick() {
                    if (numberOfHumans == 2) {
                        loader.game!!.undo(1) //Undo 1 turn in human vs human
                    } else {
                        loader.game!!.undo(2) //Undo 2 turns in human vs computer
                    }
                }
            })
        }
    }

    @FXML
    private fun initialize() {
        //Add the board to its container
        boardContainer!!.children.add(boardPane)

        //Add the actions to the list
        drawerList!!.items = actions

        //Add a click listener to the list to register when an item was pressed
        //TODO Change because throws error if called before item is selected
        drawerList!!.setOnMouseClicked { _ ->
            drawerList!!.selectionModel.selectedItem.onClick()
            drawerList!!.selectionModel.clearSelection()
        }

        //Animate the hamburger when the drawer opens/closes
        val animationRate = Math.abs(hamburger!!.animation.rate)

        drawer!!.setOnDrawerOpening { _ ->
            val burgerAnimation = hamburger!!.animation
            burgerAnimation.rate = animationRate
            burgerAnimation.play()
        }

        drawer!!.setOnDrawerClosing { _ ->
            val burgerAnimation = hamburger!!.animation
            burgerAnimation.rate = -animationRate
            burgerAnimation.play()
        }

        loader.game!!.notifyNextPlayer() //Start the game
    }

    @FXML
    private fun handleHamburgerClick() {
        drawer!!.toggle() //When the hamburger is pressed open/close the drawer
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
    internal abstract inner class Action {

        /**
         * @return the actions name (to be displayed)
         */
        internal abstract val name: String

        /**
         * What to run when the action is clicked
         */
        internal abstract fun onClick()

        override fun toString(): String {
            return name //Defines the string to display when the item is put in the list view
        }
    }
}
