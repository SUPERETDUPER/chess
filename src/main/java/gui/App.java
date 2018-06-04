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
import modele.joueur.Joueur;

public class App extends Application {
    @FunctionalInterface
    public interface MontrerIntro {
        void montrerIntro();
    }

    @FunctionalInterface
    public interface MontrerJeu {
        void montrerJeu(Joueur premierJoueur, Joueur deuxiemeJoueur);
    }

    private static final String TITRE = "Échec et Mat";

    private final Scene scene = new Scene(new Pane());
    private final Parent intro = new IntroRoot(this::montrerJeu).getRoot();

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

        scene.setRoot(intro);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    private void montrerJeu(Joueur premierJoueur, Joueur deuxiemeJoueur) {
        changerRoot(new JeuScene(premierJoueur, deuxiemeJoueur, this::montrerIntro).getRoot());
    }

    private void montrerIntro() {
        changerRoot(intro);
    }

    private void changerRoot(Parent nouveauRoot) {
        Parent pastRoot = scene.getRoot();
        pastRoot.setCacheHint(CacheHint.SPEED);

        FadeTransition fadeOut = new FadeTransition(new Duration(1000), pastRoot);
        fadeOut.setFromValue(100);
        fadeOut.setToValue(0);

        nouveauRoot.setOpacity(0);
        nouveauRoot.setCacheHint(CacheHint.SPEED);

        FadeTransition fadeIn = new FadeTransition(new Duration(1000), nouveauRoot);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(100);
        fadeIn.setOnFinished(event -> {
            nouveauRoot.setCacheHint(CacheHint.DEFAULT);
            pastRoot.setCacheHint(CacheHint.DEFAULT);
        });


        fadeOut.setOnFinished(event -> {
            scene.setRoot(nouveauRoot);
            fadeIn.play();
        });

        fadeOut.play();
    }
}
