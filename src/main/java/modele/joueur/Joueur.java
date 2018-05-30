package modele.joueur;

import modele.moves.Mouvement;
import modele.pieces.Couleur;

import java.util.function.Consumer;

/**
 * Un joueur. Chaque joueur a une couleur.
 */
public abstract class Joueur {
    private final Couleur couleur;

    Joueur(Couleur couleur) {
        this.couleur = couleur;
    }

    /**
     * @return la couleur du joueur
     */
    public Couleur getCouleur() {
        return couleur;
    }

    /**
     * Demande au joueur de soumettre son prochain mouvement par l'entremise du callback
     *
     * @param callback la méthode par laquelle l'on soumet son prochain mouvement
     */
    public abstract void getMouvement(Consumer<Mouvement> callback);
}
