package modele.joueur;

import gui.MoveListener;
import modele.Modele;
import modele.moves.Move;

public class JoueurHumain implements Joueur, MoveListener {
    private Modele.MoveCallback moveCallback;

    @Override
    public void notifierTour(Modele.MoveCallback moveCallback) {
        this.moveCallback = moveCallback;
        System.out.println("Notified tour");
    }

    @Override
    public void notifyMove(Move move) {
        System.out.println("Notified: " + (moveCallback != null));
        if (moveCallback != null) {
            moveCallback.jouer(move);
            moveCallback = null;
        }
    }
}
