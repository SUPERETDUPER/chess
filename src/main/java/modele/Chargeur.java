package modele;

import modele.joueur.Joueur;
import modele.pieces.Roi;
import modele.util.Couleur;
import modele.util.Plateau;

import java.io.*;
import java.util.EnumMap;

public class Chargeur {
    private File file = new File("dernierePartie.txt");
    private Jeu jeu;

    private void sauvgarder() {
        try {
            if (!file.exists()) file.createNewFile();

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
            objectOutputStream.writeObject(jeu);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean peutCharger() {
        return file.exists();
    }

    public boolean chargerDuFichier() {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
            this.jeu = (Jeu) objectInputStream.readObject();
            this.jeu.tourAProperty().addListener((observable, oldValue, newValue) -> sauvgarder());
            objectInputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void creeNouveauJeu(EnumMap<Couleur, Joueur> joueurs) {
        //Créer les rois
        Roi roiNoir = new Roi(Couleur.NOIR);
        Roi roiBlanc = new Roi(Couleur.BLANC);

        //Créer le modèle de jeu
        JeuData jeuData = new JeuData(Plateau.creePlateauDebut(roiNoir, roiBlanc), roiBlanc, roiNoir);

        //Créer et commencer la partie
        this.jeu = new Jeu(jeuData, joueurs);
        jeu.tourAProperty().addListener((observable, oldValue, newValue) -> sauvgarder());
    }

    public Jeu getJeu() {
        return jeu;
    }
}
