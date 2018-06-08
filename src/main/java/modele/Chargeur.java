package modele;

import modele.joueur.Joueur;
import modele.pieces.*;
import modele.plateau.Plateau;
import modele.plateau.Position;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.EnumMap;

public class Chargeur {
    private File file = new File("dernierePartie.txt");
    private Jeu jeu;

    public void sauvgarder() {
        try {
            if (!file.exists()) file.createNewFile();

            new ObjectOutputStream(new FileOutputStream(file)).writeObject(jeu);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean peutCharger() {
        return file.exists();
    }

    public void chargerDuFichier() {
        try {
            this.jeu = (Jeu) new ObjectInputStream(new FileInputStream(file)).readObject();
            this.jeu.tourAProperty().addListener((observable, oldValue, newValue) -> sauvgarder());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void creeNouveau(EnumMap<Couleur, Joueur> joueurs) {
        //Créer les rois
        Roi roiNoir = new Roi(Couleur.NOIR);
        Roi roiBlanc = new Roi(Couleur.BLANC);

        //Créer le modèle de jeu
        JeuData jeuData = new JeuData(creePlateau(roiNoir, roiBlanc), roiBlanc, roiNoir);

        //Créer et commencer la partie
        this.jeu = new Jeu(jeuData, joueurs);
        jeu.tourAProperty().addListener((observable, oldValue, newValue) -> sauvgarder());
    }

    public Jeu getJeu() {
        return jeu;
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
