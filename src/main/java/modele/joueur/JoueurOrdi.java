package modele.joueur;

import modele.JeuData;
import modele.MoveCallbackWrapper;
import modele.moves.Move;
import modele.pieces.Couleur;

import java.util.Set;

public class JoueurOrdi implements Joueur {
    private final Couleur couleur;
    private JeuData jeuData;

    public JoueurOrdi(JeuData jeuData, Couleur couleur) {
        this.jeuData = jeuData;
        this.couleur = couleur;
    }

    @Override
    public void notifierTour(MoveCallbackWrapper moveCallbackWrapper) {
        Set<Move> moves = jeuData.getAllLegalMoves(couleur);

        Move bestMove = moves.iterator().next();

        for (Move move : moves) {
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