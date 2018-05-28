package modele.joueur;

import gui.BoardController;
import modele.MoveCallbackWrapper;

public class JoueurHumain implements Joueur {
    private final BoardController boardController;
    private final boolean isBlanc;

    public JoueurHumain(BoardController boardController, boolean isBlanc) {
        this.boardController = boardController;
        this.isBlanc = isBlanc;
    }

    @Override
    public void notifierTour(MoveCallbackWrapper moveCallbackWrapper) {
        boardController.getTour(isBlanc, moveCallbackWrapper);
    }

    @Override
    public boolean isBlanc() {
        return isBlanc;
    }
}
