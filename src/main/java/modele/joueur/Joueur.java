package modele.joueur;

import gui.jeu.board.Board;
import modele.JeuData;
import modele.moves.Mouvement;
import modele.pieces.Couleur;

import java.util.function.Consumer;

/**
 * Un joueur. Chaque joueur a une couleur.
 */
public abstract class Joueur {
    /**
     * Demande au joueur de soumettre son prochain mouvement par l'entremise du callback
     *
     * @param callback la méthode par laquelle l'on soumet son prochain mouvement
     * @param couleur la couleur du mouvement requis
     */
    public abstract void getMouvement(Consumer<Mouvement> callback, Couleur couleur);

    public abstract void initialize(JeuData jeuData, Board board);

    abstract String getNom();

    @Override
    public String toString() {
        return getNom();
    }
}
