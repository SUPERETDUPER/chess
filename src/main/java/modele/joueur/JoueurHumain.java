package modele.joueur;

import gui.jeu.board.Board;
import gui.jeu.board.DemandeDeMouvement;
import modele.JeuData;
import modele.moves.Mouvement;
import modele.pieces.Couleur;

import java.util.function.Consumer;

/**
 * Un joueur qui utilise le plateau de jeu pour soumettre ses mouvements
 */
public class JoueurHumain extends Joueur {
    private Board board;

    @Override
    public void initialize(JeuData jeuData, Board board) {
        this.board = board;
    }

    /**
     * Quand l'on a besoin du prochain movement, l'objet crée une demande pour le plateau de jeu
     *
     * @param callback la méthode par laquelle l'on soumet son prochain mouvement
     */
    @Override
    public void getMouvement(Consumer<Mouvement> callback, Couleur couleur) {
        board.demanderMouvement(new DemandeDeMouvement(callback, couleur));
    }

    @Override
    String getNom() {
        return "Humain";
    }
}
