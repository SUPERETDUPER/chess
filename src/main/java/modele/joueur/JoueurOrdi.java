package modele.joueur;

import modele.Helper;
import modele.Jeu;
import modele.MoveCallbackWrapper;
import modele.moves.Move;

import java.util.Set;

public class JoueurOrdi implements Joueur {
    private final Jeu jeu;
    private final boolean isWhite;

    public JoueurOrdi(Jeu jeu, boolean isWhite) {
        this.jeu = jeu;
        this.isWhite = isWhite;
    }

    @Override
    public void notifierTour(MoveCallbackWrapper moveCallbackWrapper) {
        Set<Move> moves = Helper.getAllLegalMoves(isWhite, jeu.getBoard(), jeu.getRoi(isWhite));

        Move bestMove = null;

        for (Move move : moves) {
            if (bestMove == null) {
                bestMove = move;
                continue;
            }

            if (isWhite) {
                if (move.getValue() < bestMove.getValue()) {
                    bestMove = move;
                }
            } else {
                if (move.getValue() > bestMove.getValue()) {
                    bestMove = move;
                }
            }
        }

        moveCallbackWrapper.jouer(bestMove);
    }

    @Override
    public boolean isBlanc() {
        return isWhite;
    }
}