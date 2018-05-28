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

        Move bestMove = null;

        for (Move move : moves) {
            if (bestMove == null) {
                bestMove = move;
                continue;
            }

            if (moveCallbackWrapper.isWhite()) {
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
}