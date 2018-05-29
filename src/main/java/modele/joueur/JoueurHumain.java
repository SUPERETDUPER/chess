package modele.joueur;

import gui.BoardController;
import gui.DemandeDeMouvement;
import modele.moves.Move;
import modele.pieces.Couleur;

import java.util.function.Consumer;

/**
 * Un joueur qui utilise le plateau de jeu pour soumettre ses mouvements
 */
public class JoueurHumain extends Joueur {
    private final BoardController boardController;

    public JoueurHumain(BoardController boardController, Couleur couleur) {
        super(couleur);
        this.boardController = boardController;
    }

    /**
     * Quand l'on a besoin du prochain movement, l'objet crée une demande pour le plateau de jeu
     *
     * @param callback la méthode par laquelle l'on soumet son prochain mouvement
     */
    @Override
    public void getMouvement(Consumer<Move> callback) {
        boardController.demanderMouvement(new DemandeDeMouvement(callback, this.getCouleur()));
    }
}
