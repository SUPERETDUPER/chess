package ui

import javafx.animation.FadeTransition
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.CacheHint
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.layout.Pane
import javafx.stage.Stage
import javafx.util.Duration
import engine.Loader
import engine.player.Player
import engine.util.Colour
import ui.game.GameController
import ui.intro.IntroController
import java.io.IOException
import java.net.URL
import java.util.*

private const val TITLE = "Chess"

fun main(args: Array<String>) {
    Application.launch(Main::class.java, *args) //Starts the ui
}

class Main : Application() {

    /**
     * The loader will load the game engine
     */
    private val loader = Loader()

    private val scene = Scene(Pane()) //The new Pane() is just temporary and will be replaced

    /**
     * The main menu page
     */
    private val intro = loadFromFXML(IntroController(this::startNewGame), javaClass.getResource("/intro.fxml"))

    /**
     * Starts the UI
     */
    override fun start(primaryStage: Stage) {
        primaryStage.title = TITLE
        primaryStage.scene = scene

        //If loading the game from file succeeds go immediately to game
        if (loader.loadGameFromFile()) {
            scene.root = loadFromFXML(
                    GameController(
                            { switchRoot(intro) },
                            loader
                    ),
                    javaClass.getResource("/jeu.fxml")
            )
        } else {
            scene.root = intro //If could not load from file go to intro page
        }

        primaryStage.isMaximized = true
        primaryStage.show()
    }

    /**
     * @param players the players for the new game
     */
    //TODO pass in command but not the actual players
    private fun startNewGame(players: EnumMap<Colour, Player>) {
        loader.createNewGame(players) //Creates a new game
        switchRoot(loadFromFXML(GameController({ switchRoot(intro) }, loader), javaClass.getResource("/jeu.fxml"))) //Go to game page
    }

    /**
     * Switches the page with a fade transition
     */
    private fun switchRoot(newRoot: Parent) {
        val pastRoot = scene.root
        pastRoot.cacheHint = CacheHint.SPEED

        val fadeOut = FadeTransition(Duration(1000.0), pastRoot)
        fadeOut.fromValue = 100.0
        fadeOut.toValue = 0.0

        newRoot.opacity = 0.0
        newRoot.cacheHint = CacheHint.SPEED

        val fadeIn = FadeTransition(Duration(1000.0), newRoot)
        fadeIn.fromValue = 0.0
        fadeIn.toValue = 100.0
        fadeIn.setOnFinished { _ ->
            newRoot.cacheHint = CacheHint.DEFAULT
            pastRoot.cacheHint = CacheHint.DEFAULT
        }

        fadeOut.setOnFinished { _ ->
            scene.root = newRoot
            fadeIn.play()
        }

        fadeOut.play()
    }

    /**
     * @param controller the controller for the FXML view
     * @param url        the url of the FXML
     * @return the view loaded from the FXML
     */
    private fun loadFromFXML(controller: Any, url: URL): Parent {
        //Create the loader with the url
        val fxmlLoader = FXMLLoader(url)
        fxmlLoader.setController(controller) //Attach the controller

        //Load and return view from FXML
        try {
            return fxmlLoader.load()
        } catch (e: IOException) {
            throw RuntimeException(e)
        }

    }
}
