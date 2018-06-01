package gui;

import gui.intro.IntroRoot;
import gui.jeu.JeuScene;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

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
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle(TITRE);
        primaryStage.setScene(scene);

//        primaryStage.setScene(new JeuScene(true, false).getRoot());
        scene.setRoot(new IntroRoot(this::montrerJeu).getRoot());
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    private void montrerJeu(boolean isJoueurBlancHumain, boolean isJoueurNoirHumain) throws IOException {
        scene.setRoot(new JeuScene(isJoueurBlancHumain, isJoueurNoirHumain).getRoot());
    }
}
