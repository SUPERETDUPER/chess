package modele;

import modele.joueur.Joueur;
import modele.util.Couleur;
import modele.util.Plateau;

import java.io.*;
import java.util.EnumMap;

/**
 * Responsable de sauvgarder et recharger le fichier contenant le Jeu
 */
public class Chargeur {
    private final File file = new File("dernierePartie.txt"); //Le nom du fichier
    private Jeu jeu;

    /**
     * Sauvegarde le jeu
     */
    private void sauvgarder() {
        try {
            if (!file.exists()) //noinspection ResultOfMethodCallIgnored
                file.createNewFile(); //Si le fichier de jeu n'existe pas créer le fichier

            //Écrire le jeu au fichier
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
            objectOutputStream.writeObject(jeu);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * charge le jeu du fichier
     *
     * @return Si on a chargé le jeu avec success
     */
    public boolean chargerDuFichier() {
        if (!file.exists()) return false; //Si aucun fichier -> échec

        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
            this.jeu = (Jeu) objectInputStream.readObject(); //Lire le jeu
            this.jeu.tourAProperty().addListener((observable, oldValue, newValue) -> sauvgarder()); //Quand le jeu change sauvegarder
            objectInputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void creeNouveauJeu(EnumMap<Couleur, Joueur> joueurs) {
        //Créer les rois

        //Créer le modèle de jeu
        JeuData jeuData = new JeuData(Plateau.creePlateauDebut());

        //Créer et notifierProchainJoueur la partie
        this.jeu = new Jeu(jeuData, joueurs);
        jeu.tourAProperty().addListener((observable, oldValue, newValue) -> sauvgarder());
    }

    public Jeu getJeu() {
        return jeu;
    }
}
