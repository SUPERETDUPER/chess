package gui;

import gui.intro.IntroRoot;
import gui.jeu.JeuScene;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.scene.CacheHint;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class App extends Application {
    @FunctionalInterface
    public interface MontrerJeu {
        void montrer(boolean isJoueurBlancHumain, boolean isJoueurNoirHumain) throws IOException;
    }

    private static final String TITRE = "Échec et Mat";

    private final Scene scene = new Scene(new Pane());

    public static void main(String[] args) {
        //Commencer l'interface graphique
        launch(args);
    }

    /**
     * Commence l'interface graphique
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle(TITRE);
        primaryStage.setScene(scene);

        scene.setRoot(new IntroRoot(this::montrerJeu).getRoot());
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    private void montrerJeu(boolean isJoueurBlancHumain, boolean isJoueurNoirHumain) {
        Parent pastRoot = scene.getRoot();
        pastRoot.setCacheHint(CacheHint.SPEED);

        FadeTransition fadeOut = new FadeTransition(new Duration(1000), pastRoot);
        fadeOut.setFromValue(100);
        fadeOut.setToValue(0);

        Parent jeuRoot = new JeuScene(isJoueurBlancHumain, isJoueurNoirHumain).getRoot();
        jeuRoot.setOpacity(0);
        jeuRoot.setCacheHint(CacheHint.SPEED);

        FadeTransition fadeIn = new FadeTransition(new Duration(1000), jeuRoot);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(100);
        fadeIn.setOnFinished(event -> {
            jeuRoot.setCacheHint(CacheHint.DEFAULT);
            pastRoot.setCacheHint(CacheHint.DEFAULT);
        });


        fadeOut.setOnFinished(event -> {
            scene.setRoot(jeuRoot);
            fadeIn.play();
        });

        fadeOut.play();
    }
}
