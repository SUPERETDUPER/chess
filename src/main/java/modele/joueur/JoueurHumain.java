package modele.joueur;

import gui.BoardController;
import gui.MoveRequest;
import modele.moves.Move;
import modele.pieces.Couleur;

import java.util.function.Consumer;

public class JoueurHumain implements Joueur {
    private final BoardController boardController;
    private final Couleur couleur;

    public JoueurHumain(BoardController boardController, Couleur couleur) {
        this.boardController = boardController;
        this.couleur = couleur;
    }

    @Override
    public void getMouvement(Consumer<Move> callback) {
        boardController.requestMove(new MoveRequest(callback, couleur));
    }

    @Override
    public Couleur getCouleur() {
        return couleur;
    }
}
