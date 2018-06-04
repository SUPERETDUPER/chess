package modele.joueur;

import gui.jeu.board.BoardController;
import gui.jeu.board.DemandeDeMouvement;
import modele.JeuData;
import modele.moves.Mouvement;
import modele.pieces.Couleur;

import java.util.function.Consumer;

/**
 * Un joueur qui utilise le plateau de jeu pour soumettre ses mouvements
 */
public class JoueurHumain extends Joueur {
    private BoardController boardController;

    @Override
    public void initialize(JeuData jeuData, BoardController boardController) {
        this.boardController = boardController;
    }

    /**
     * Quand l'on a besoin du prochain movement, l'objet crée une demande pour le plateau de jeu
     *
     * @param callback la méthode par laquelle l'on soumet son prochain mouvement
     */
    @Override
    public void getMouvement(Consumer<Mouvement> callback, Couleur couleur) {
        boardController.demanderMouvement(new DemandeDeMouvement(callback, couleur));
    }

    @Override
    String getNom() {
        return "Humain";
    }
}
