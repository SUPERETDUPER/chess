package gui.jeu;

import gui.App;
import gui.jeu.board.Board;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import modele.Jeu;
import modele.JeuData;
import modele.joueur.Joueur;
import modele.pieces.*;
import modele.plateau.Plateau;
import modele.plateau.Position;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.EnumMap;

public class JeuScene {

    private final Parent root;

    public JeuScene(EnumMap<Couleur, Joueur> joueurs, App.MontrerIntro goBack) {
        //Créer les rois
        Roi roiNoir = new Roi(Couleur.NOIR);
        Roi roiBlanc = new Roi(Couleur.BLANC);

        //Créer le modèle de jeu
        JeuData jeuData = new JeuData(creePlateau(roiNoir, roiBlanc), roiBlanc, roiNoir);

        //Créer le controller d'interface
        Board board = new Board(jeuData);

        //Créer l'interface
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/jeu.fxml"));

        fxmlLoader.setControllerFactory(param -> {
            if (param == JeuController.class) {
                return new JeuController(goBack, board);
            }

            throw new RuntimeException("Aucun controlleur connue");
        });

        //Charger l'interface
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Ajouter et créer les joueurs
        for (Joueur joueur : joueurs.values()) {
            joueur.initialize(jeuData, board);
        }

        //Créer et commencer la partie
        new Jeu(jeuData, joueurs).commencer();
    }

    public Parent getRoot() {
        return root;
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

}
