package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import modele.Jeu;
import modele.JeuData;
import modele.joueur.JoueurHumain;
import modele.joueur.JoueurOrdi;
import modele.pieces.*;
import modele.plateau.Plateau;
import modele.plateau.Position;
import org.jetbrains.annotations.NotNull;

public class App extends Application {
    private static final String TITRE = "Échec et Mat";

    private static JeuData jeuData;

    public static void main(String[] args) {
        //Créer les rois
        Roi roiNoir = new Roi(Couleur.NOIR);
        Roi roiBlanc = new Roi(Couleur.BLANC);

        //Créer le modèle de jeu
        jeuData = new JeuData(creePlateau(roiNoir, roiBlanc), roiBlanc, roiNoir);

        //Commencer l'interface graphique
        launch(args);
    }

    /**
     * Retourne un nouveau plateau avec la position du début
     *
     * @param roiNoir  le roi noir à utiliser
     * @param roiBlanc le roi blanc à utiliser
     * @return le plateau en position initiale
     */
    @NotNull
    private static Plateau creePlateau(Roi roiNoir, Roi roiBlanc) {
        Plateau plateau = new Plateau();
        plateau.ajouter(new Position(7, 0), new Tour(Couleur.BLANC));
        plateau.ajouter(new Position(7, 1), new Cavalier(Couleur.BLANC));
        plateau.ajouter(new Position(7, 2), new Fou(Couleur.BLANC));
        plateau.ajouter(new Position(7, 3), new Dame(Couleur.BLANC));
        plateau.ajouter(new Position(7, 4), roiBlanc);
        plateau.ajouter(new Position(7, 5), new Fou(Couleur.BLANC));
        plateau.ajouter(new Position(7, 6), new Cavalier(Couleur.BLANC));
        plateau.ajouter(new Position(7, 7), new Tour(Couleur.BLANC));

        plateau.ajouter(new Position(6, 0), new Pion(Couleur.BLANC));
        plateau.ajouter(new Position(6, 1), new Pion(Couleur.BLANC));
        plateau.ajouter(new Position(6, 2), new Pion(Couleur.BLANC));
        plateau.ajouter(new Position(6, 3), new Pion(Couleur.BLANC));
        plateau.ajouter(new Position(6, 4), new Pion(Couleur.BLANC));
        plateau.ajouter(new Position(6, 5), new Pion(Couleur.BLANC));
        plateau.ajouter(new Position(6, 6), new Pion(Couleur.BLANC));
        plateau.ajouter(new Position(6, 7), new Pion(Couleur.BLANC));

        plateau.ajouter(new Position(1, 0), new Pion(Couleur.NOIR));
        plateau.ajouter(new Position(1, 1), new Pion(Couleur.NOIR));
        plateau.ajouter(new Position(1, 2), new Pion(Couleur.NOIR));
        plateau.ajouter(new Position(1, 3), new Pion(Couleur.NOIR));
        plateau.ajouter(new Position(1, 4), new Pion(Couleur.NOIR));
        plateau.ajouter(new Position(1, 5), new Pion(Couleur.NOIR));
        plateau.ajouter(new Position(1, 6), new Pion(Couleur.NOIR));
        plateau.ajouter(new Position(1, 7), new Pion(Couleur.NOIR));

        plateau.ajouter(new Position(0, 0), new Tour(Couleur.NOIR));
        plateau.ajouter(new Position(0, 1), new Cavalier(Couleur.NOIR));
        plateau.ajouter(new Position(0, 2), new Fou(Couleur.NOIR));
        plateau.ajouter(new Position(0, 3), new Dame(Couleur.NOIR));
        plateau.ajouter(new Position(0, 4), roiNoir);
        plateau.ajouter(new Position(0, 5), new Fou(Couleur.NOIR));
        plateau.ajouter(new Position(0, 6), new Cavalier(Couleur.NOIR));
        plateau.ajouter(new Position(0, 7), new Tour(Couleur.NOIR));

        return plateau;
    }

    /**
     * Commence l'interface graphique
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle(TITRE); //Définir le titre de la fenêtre

        //Créer le controller d'interface
        BoardController boardController = new BoardController(jeuData);

        //Créer l'interface
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/board.fxml"));
        fxmlLoader.setController(boardController);

        //Montrer l'interface
        primaryStage.setScene(new Scene(fxmlLoader.load()));
        primaryStage.setMaximized(true);
        primaryStage.show();

        //Créer le jeu
        Jeu jeu = new Jeu(jeuData);

        //Ajouter et créer les joueurs
        jeu.ajouterJoueur(new JoueurHumain(boardController, Couleur.NOIR));
        jeu.ajouterJoueur(new JoueurOrdi(jeuData, Couleur.BLANC));

        //Commencer la partie
        jeu.commencer();
    }
}