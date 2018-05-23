package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import modele.Modele;
import modele.board.Board;
import modele.board.Position;
import modele.pieces.Roi;

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
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/board.fxml"));

        Board board = new Board();
        board.ajouter(new Position(0, 0), new Roi(true));
        BoardController controller = new BoardController(new Modele(board));
        fxmlLoader.setController(controller);

        primaryStage.setScene(
                new Scene(
                        fxmlLoader.load()
                )
        );

        //Montrer l'interface
        primaryStage.setMaximized(true);
        primaryStage.show();
    }
}
