package modele.joueur;

import gui.BoardController;
import modele.MoveCallbackWrapper;
import modele.pieces.Couleur;

public class JoueurHumain implements Joueur {
    private final BoardController boardController;
    private final Couleur couleur;

    public JoueurHumain(BoardController boardController, Couleur couleur) {
        this.boardController = boardController;
        this.couleur = couleur;
    }

    @Override
    public void notifierTour(MoveCallbackWrapper moveCallbackWrapper) {
        boardController.getTour(couleur, moveCallbackWrapper);
    }

    @Override
    public Couleur getCouleur() {
        return couleur;
    }
}
