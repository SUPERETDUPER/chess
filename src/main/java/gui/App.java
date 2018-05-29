package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import modele.Jeu;
import modele.JeuData;
import modele.board.Board;
import modele.board.Position;
import modele.joueur.JoueurHumain;
import modele.joueur.JoueurOrdi;
import modele.pieces.*;
import org.jetbrains.annotations.NotNull;

public class App extends Application {
    private static final String TITRE = "Échec et Mat";

    private static JeuData jeuData;

    public static void main(String[] args) {
        Roi roiNoir = new Roi(Couleur.NOIR);
        Roi roiBlanc = new Roi(Couleur.BLANC);

        jeuData = new JeuData(getBoard(roiNoir, roiBlanc), roiBlanc, roiNoir);

        launch(args);
    }

    @NotNull
    private static Board getBoard(Roi roiNoir, Roi roiBlanc) {
        Board board = new Board();
        board.ajouter(new Position(0, 0), new Tour(Couleur.BLANC));
        board.ajouter(new Position(0, 1), new Cavalier(Couleur.BLANC));
        board.ajouter(new Position(0, 2), new Fou(Couleur.BLANC));
        board.ajouter(new Position(0, 3), roiBlanc);
        board.ajouter(new Position(0, 4), new Dame(Couleur.BLANC));
        board.ajouter(new Position(0, 5), new Fou(Couleur.BLANC));
        board.ajouter(new Position(0, 6), new Cavalier(Couleur.BLANC));
        board.ajouter(new Position(0, 7), new Tour(Couleur.BLANC));

        board.ajouter(new Position(1, 0), new Pion(Couleur.BLANC));
        board.ajouter(new Position(1, 1), new Pion(Couleur.BLANC));
        board.ajouter(new Position(1, 2), new Pion(Couleur.BLANC));
        board.ajouter(new Position(1, 3), new Pion(Couleur.BLANC));
        board.ajouter(new Position(1, 4), new Pion(Couleur.BLANC));
        board.ajouter(new Position(1, 5), new Pion(Couleur.BLANC));
        board.ajouter(new Position(1, 6), new Pion(Couleur.BLANC));
        board.ajouter(new Position(1, 7), new Pion(Couleur.BLANC));

        board.ajouter(new Position(6, 0), new Pion(Couleur.NOIR));
        board.ajouter(new Position(6, 1), new Pion(Couleur.NOIR));
        board.ajouter(new Position(6, 2), new Pion(Couleur.NOIR));
        board.ajouter(new Position(6, 3), new Pion(Couleur.NOIR));
        board.ajouter(new Position(6, 4), new Pion(Couleur.NOIR));
        board.ajouter(new Position(6, 5), new Pion(Couleur.NOIR));
        board.ajouter(new Position(6, 6), new Pion(Couleur.NOIR));
        board.ajouter(new Position(6, 7), new Pion(Couleur.NOIR));

        board.ajouter(new Position(7, 0), new Tour(Couleur.NOIR));
        board.ajouter(new Position(7, 1), new Cavalier(Couleur.NOIR));
        board.ajouter(new Position(7, 2), new Fou(Couleur.NOIR));
        board.ajouter(new Position(7, 3), roiNoir);
        board.ajouter(new Position(7, 4), new Dame(Couleur.NOIR));
        board.ajouter(new Position(7, 5), new Fou(Couleur.NOIR));
        board.ajouter(new Position(7, 6), new Cavalier(Couleur.NOIR));
        board.ajouter(new Position(7, 7), new Tour(Couleur.NOIR));

        return board;
    }

    /**
     * Commence l'interface graphique
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle(TITRE); //Définir le titre

        //Load l'interface
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/board.fxml"));

        BoardController controller = new BoardController(jeuData);

        fxmlLoader.setController(controller);

        primaryStage.setScene(
                new Scene(
                        fxmlLoader.load()
                )
        );

        //Montrer l'interface
        primaryStage.setMaximized(true);
        primaryStage.show();

        jeuData.ajouterJoueur(new JoueurHumain(controller, Couleur.BLANC));
        jeuData.ajouterJoueur(new JoueurOrdi(jeuData, Couleur.NOIR));
        new Jeu(jeuData).commencer();
    }
}