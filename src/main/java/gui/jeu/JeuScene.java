package gui.jeu;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import modele.Jeu;
import modele.JeuData;
import modele.joueur.JoueurHumain;
import modele.joueur.JoueurOrdi;
import modele.pieces.*;
import modele.plateau.Plateau;
import modele.plateau.Position;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class JeuScene {

    private final Parent root;

    public JeuScene(boolean isJoueurBlancHumain, boolean isJoueurNoirHumain) throws IOException {
        //Créer les rois
        Roi roiNoir = new Roi(Couleur.NOIR);
        Roi roiBlanc = new Roi(Couleur.BLANC);

        //Créer le modèle de jeu
        JeuData jeuData = new JeuData(creePlateau(roiNoir, roiBlanc), roiBlanc, roiNoir);

        //Créer le controller d'interface
        BoardController boardController = new BoardController(jeuData);

        //Créer l'interface
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/board.fxml"));
        fxmlLoader.setController(boardController);

//        //Montrer l'interface
        root = fxmlLoader.load();

        //Créer le jeu
        Jeu jeu = new Jeu(jeuData);

        //Ajouter et créer les joueurs
        if (isJoueurBlancHumain) jeu.ajouterJoueur(new JoueurHumain(boardController, Couleur.BLANC));
        else jeu.ajouterJoueur(new JoueurOrdi(jeuData, Couleur.BLANC));

        if (isJoueurNoirHumain) jeu.ajouterJoueur(new JoueurHumain(boardController, Couleur.NOIR));
        else jeu.ajouterJoueur(new JoueurOrdi(jeuData, Couleur.NOIR));

        //Commencer la partie
        jeu.commencer();
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
