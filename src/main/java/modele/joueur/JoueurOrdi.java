package modele.joueur;

import modele.Jeu;
import modele.MoveCallbackWrapper;
import modele.moves.Move;
import modele.pieces.Couleur;

import java.util.Set;

public class JoueurOrdi implements Joueur {
    private final Jeu jeu;
    private final Couleur couleur;

    public JoueurOrdi(Jeu jeu, Couleur couleur) {
        this.jeu = jeu;
        this.couleur = couleur;
    }

    @Override
    public void notifierTour(MoveCallbackWrapper moveCallbackWrapper) {
        Set<Move> moves = Jeu.getAllLegalMoves(jeu, couleur);

        Move bestMove = null;

        for (Move move : moves) {
            if (bestMove == null) {
                bestMove = move;
                continue;
            }

            if (couleur == Couleur.BLANC) {
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
    public Couleur getCouleur() {
        return couleur;
    }
}