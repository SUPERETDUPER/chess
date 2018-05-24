package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import modele.Modele;
import modele.board.Board;
import modele.board.Position;
import modele.pieces.*;

public class App extends Application {
    private static final String TITRE = "Échec et Mat";

    private static Modele modele;

    public static void main(String[] args) {
        Board board = new Board();
        board.ajouter(new Position(0, 0), new Tour(true));
        board.ajouter(new Position(0, 1), new Cavalier(true));
        board.ajouter(new Position(0, 2), new Fou(true));
        board.ajouter(new Position(0, 3), new Roi(true));
        board.ajouter(new Position(0, 4), new Dame(true));
        board.ajouter(new Position(0, 5), new Fou(true));
        board.ajouter(new Position(0, 6), new Cavalier(true));
        board.ajouter(new Position(0, 7), new Tour(true));

        board.ajouter(new Position(1, 0), new Pion(true));
        board.ajouter(new Position(1, 1), new Pion(true));
        board.ajouter(new Position(1, 2), new Pion(true));
        board.ajouter(new Position(1, 3), new Pion(true));
        board.ajouter(new Position(1, 4), new Pion(true));
        board.ajouter(new Position(1, 5), new Pion(true));
        board.ajouter(new Position(1, 6), new Pion(true));
        board.ajouter(new Position(1, 7), new Pion(true));

        board.ajouter(new Position(6, 0), new Pion(false));
        board.ajouter(new Position(6, 1), new Pion(false));
        board.ajouter(new Position(6, 2), new Pion(false));
        board.ajouter(new Position(6, 3), new Pion(false));
        board.ajouter(new Position(6, 4), new Pion(false));
        board.ajouter(new Position(6, 5), new Pion(false));
        board.ajouter(new Position(6, 6), new Pion(false));
        board.ajouter(new Position(6, 7), new Pion(false));

        board.ajouter(new Position(7, 0), new Tour(false));
        board.ajouter(new Position(7, 1), new Cavalier(false));
        board.ajouter(new Position(7, 2), new Fou(false));
        board.ajouter(new Position(7, 3), new Roi(false));
        board.ajouter(new Position(7, 4), new Dame(false));
        board.ajouter(new Position(7, 5), new Fou(false));
        board.ajouter(new Position(7, 6), new Cavalier(false));
        board.ajouter(new Position(7, 7), new Tour(false));
        modele = new Modele(board);

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

        BoardController controller = new BoardController(modele);
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
