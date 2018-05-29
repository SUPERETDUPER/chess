package modele.joueur;

import modele.JeuData;
import modele.moves.Move;
import modele.pieces.Couleur;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

//TODO Upgrade algorithm to min/max with alpha-beta pruning

/**
 * Un joueur qui utilise un algorithm pour trouver son prochain mouvement
 */
public class JoueurOrdi extends Joueur {
    private final JeuData jeuData;
    private final static int MAX_DEPTH = 4;

    public JoueurOrdi(JeuData jeuData, Couleur couleur) {
        super(couleur);
        this.jeuData = jeuData;
    }

    /**
     * Trouve tous les mouvement possible puis retourne celui qui remporte le plus de points
     *
     * @param callback la méthode par laquelle l'on soumet son prochain mouvement
     */
    @Override
    public void getMouvement(Consumer<Move> callback) {
        callback.accept(calculerMeilleurMouvement(new MoveSequence(), getCouleur()).getFirst());
    }

    private MoveSequence calculerMeilleurMouvement(MoveSequence pastSequence, Couleur couleur) {
        if (pastSequence.getLength() == MAX_DEPTH) return pastSequence;

        Set<Move> moves = jeuData.getAllMoves(couleur);

        MoveSequence bestMove = null;

        for (Move move : moves) {
            move.appliquer(jeuData.getPlateau());
            MoveSequence moveSequence = calculerMeilleurMouvement(pastSequence.addAndReturn(move), oppose(couleur));

            if (bestMove == null) {
                bestMove = moveSequence;
            } else if (couleur == Couleur.BLANC) {
                if (moveSequence.getValue() > bestMove.getValue()) {
                    bestMove = moveSequence;
                }
            } else {
                if (moveSequence.getValue() < bestMove.getValue()) {
                    bestMove = moveSequence;
                }
            }

            move.undo(jeuData.getPlateau());
        }

        return bestMove == null ? pastSequence : bestMove;
    }

    private Couleur oppose(Couleur couleur) {
        return couleur == Couleur.BLANC ? Couleur.NOIR : Couleur.BLANC;
    }

    private class MoveSequence {
        private final List<Move> moves;
        private final int value;

        MoveSequence() {
            this.value = 0;
            this.moves = new ArrayList<>();

        }

        private MoveSequence(List<Move> moves, int value) {
            this.moves = moves;
            this.value = value;
        }

        MoveSequence addAndReturn(Move move) {
            List<Move> newList = new ArrayList<>(moves);
            newList.add(move);
            return new MoveSequence(newList, value + move.getValeur());
        }

        int getValue() {
            return value;
        }

        int getLength() {
            return moves.size();
        }

        Move getFirst() {
            return moves.get(0);
        }
    }
}