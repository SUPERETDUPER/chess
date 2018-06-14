package ui;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.CacheHint;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Loader;
import model.player.Player;
import model.util.Colour;
import org.jetbrains.annotations.NotNull;
import ui.game.GameController;
import ui.intro.IntroController;

import java.io.IOException;
import java.net.URL;
import java.util.EnumMap;

public class Main extends Application {
    private static final String TITRE = "Chess";

    /**
     * The loader will load the game model
     */
    private final Loader loader = new Loader();

    private final Scene scene = new Scene(new Pane()); //The new Pane() is just temporary and will be replaced

    /**
     * The main menu page
     */
    private final Parent intro = loadFromFXML(new IntroController(this::startNewGame), getClass().getResource("/intro.fxml"));

    /**
     * Starts the UI
     */
    @Override
    public void start(@NotNull Stage primaryStage) {
        primaryStage.setTitle(TITRE);
        primaryStage.setScene(scene);

        //If loading the game from file succeeds go immediately to game
        if (loader.loadGameFromFile()) {
            scene.setRoot(loadFromFXML(
                    new GameController(
                            () -> switchRoot(intro),
                            loader
                    ),
                    getClass().getResource("/jeu.fxml")
            ));
        } else {
            scene.setRoot(intro); //If could not load from file go to intro page
        }

        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    /**
     * @param players the players for the new game
     */
    //TODO pass in command but not the actual players
    private void startNewGame(EnumMap<Colour, Player> players) {
        loader.createNewGame(players); //Creates a new game
        switchRoot(loadFromFXML(new GameController(() -> switchRoot(intro), loader), getClass().getResource("/jeu.fxml"))); //Go to game page
    }

    /**
     * Switches the page with a fade transition
     */
    private void switchRoot(Parent newRoot) {
        Parent pastRoot = scene.getRoot();
        pastRoot.setCacheHint(CacheHint.SPEED);

        FadeTransition fadeOut = new FadeTransition(new Duration(1000), pastRoot);
        fadeOut.setFromValue(100);
        fadeOut.setToValue(0);

        newRoot.setOpacity(0);
        newRoot.setCacheHint(CacheHint.SPEED);

        FadeTransition fadeIn = new FadeTransition(new Duration(1000), newRoot);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(100);
        fadeIn.setOnFinished(event -> {
            newRoot.setCacheHint(CacheHint.DEFAULT);
            pastRoot.setCacheHint(CacheHint.DEFAULT);
        });

        fadeOut.setOnFinished(event -> {
            scene.setRoot(newRoot);
            fadeIn.play();
        });

        fadeOut.play();
    }

    /**
     * @param controller the controller for the FXML view
     * @param url        the url of the FXML
     * @return the view loaded from the FXML
     */
    private static Parent loadFromFXML(Object controller, URL url) {
        //Create the loader with the url
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        fxmlLoader.setController(controller); //Attach the controller

        //Load and return view from FXML
        try {
            return fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        launch(args); //Starts the ui
    }
}
