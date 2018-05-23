package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    private static final String TITRE = "Échec et Mat";

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Commence l'interface graphique
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle(TITRE); //Définir le titre

        //Load l'interface
        primaryStage.setScene(
                new Scene(
                        new FXMLLoader(
                                getClass().getResource("/board.fxml")
                        ).load()
                )
        );

        //Montrer l'interface
        primaryStage.setMaximized(true);
        primaryStage.show();
    }
}
