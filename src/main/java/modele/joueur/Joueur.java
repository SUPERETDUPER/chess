package modele.joueur;

import gui.jeu.board.PlateauPane;
import modele.Couleur;
import modele.JeuData;
import modele.moves.Mouvement;

import java.io.Serializable;
import java.util.function.Consumer;

/**
 * Un joueur. Définit comment il va joueur
 */
public abstract class Joueur implements Serializable {
    /**
     * Demande au joueur de soumettre son prochain mouvement par l'entremise du callback
     *
     * @param callback la méthode par laquelle l'on soumet son prochain mouvement
     * @param couleur  la couleur du mouvement requis
     */
    public abstract void getMouvement(Consumer<Mouvement> callback, Couleur couleur);

    /**
     * @param jeuData
     */
    public abstract void initializeJeuData(JeuData jeuData);

    public abstract void initializeInterface(PlateauPane plateauPane);

    abstract String getNom();

    @Override
    public String toString() {
        return getNom();
    }
}
