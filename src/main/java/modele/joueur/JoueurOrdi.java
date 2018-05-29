package modele.joueur;

import modele.JeuData;
import modele.moves.Move;
import modele.pieces.Couleur;

import java.util.Set;
import java.util.function.Consumer;

public class JoueurOrdi implements Joueur {
    private final Couleur couleur;
    private JeuData jeuData;

    public JoueurOrdi(JeuData jeuData, Couleur couleur) {
        this.jeuData = jeuData;
        this.couleur = couleur;
    }

    @Override
    public void getMouvement(Consumer<Move> callback) {
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

        callback.accept(bestMove);
    }

    @Override
    public Couleur getCouleur() {
        return couleur;
    }
}