package modele.joueur;

import modele.Helper;
import modele.Jeu;
import modele.MoveCallbackWrapper;
import modele.moves.Move;

import java.util.Set;

public class JoueurOrdi implements Joueur {
    private final Jeu jeu;

    public JoueurOrdi(Jeu jeu) {
        this.jeu = jeu;
    }

    @Override
    public void notifierTour(MoveCallbackWrapper moveCallbackWrapper) {
        Set<Move> moves = Helper.getAllLegalMoves(moveCallbackWrapper.isWhite(), jeu.getBoard(), jeu.getRoi(moveCallbackWrapper.isWhite()));

        int element = (int) ((moves.size() - 1) * Math.random());

        int i = 0;

        for (Move move : moves) {
            if (element == i) moveCallbackWrapper.jouer(move);
            i++;
        }
    }
}