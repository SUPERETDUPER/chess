package gui;

import gui.intro.IntroScene;
import gui.jeu.JeuScene;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @FunctionalInterface
    public interface MontrerJeu {
        void montrer(boolean isJoueurBlancHumain, boolean isJoueurNoirHumain) throws IOException;
    }

    private static final String TITRE = "Échec et Mat";

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

//        primaryStage.setScene(new JeuScene(true, false).getScene());
        primaryStage.setScene(new IntroScene((isJoueurBlancHumain, isJoueurNoirHumain) -> montrerJeu(primaryStage, isJoueurBlancHumain, isJoueurNoirHumain)).getScene());
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    private void montrerJeu(Stage primaryStage, boolean isJoueurBlancHumain, boolean isJoueurNoirHumain) throws IOException {
        primaryStage.hide();
        primaryStage.setScene(new JeuScene(isJoueurBlancHumain, isJoueurNoirHumain).getScene());
        primaryStage.show();
    }
}
