package modele.joueur;

import gui.jeu.BoardController;
import modele.JeuData;
import modele.moves.Mouvement;
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
    private JeuData jeuData;
    private final static int MAX_DEPTH = 4;

    public JoueurOrdi(Couleur couleur) {
        super(couleur);
    }

    @Override
    public void initialize(JeuData jeuData, BoardController boardController) {
        this.jeuData = jeuData;
    }

    /**
     * Trouve tous les mouvement possible puis retourne celui qui remporte le plus de points
     *
     * @param callback la méthode par laquelle l'on soumet son prochain mouvement
     */
    @Override
    public void getMouvement(Consumer<Mouvement> callback) {
        callback.accept(calculerMeilleurMouvement(new MoveSequence(), getCouleur()).getFirst());
    }

    private MoveSequence calculerMeilleurMouvement(MoveSequence pastSequence, Couleur couleur) {
        if (pastSequence.getLength() == MAX_DEPTH) return pastSequence;

        Set<Mouvement> mouvements;

        if (pastSequence.getLength() == 0) {
            mouvements = jeuData.getAllLegalMoves(couleur);
        } else {
            mouvements = jeuData.getAllMoves(couleur);
        }

        MoveSequence bestMove = null;

        for (Mouvement mouvement : mouvements) {
            mouvement.appliquer(jeuData.getPlateau());
            MoveSequence moveSequence = calculerMeilleurMouvement(pastSequence.addAndReturn(mouvement), oppose(couleur));

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

            mouvement.undo(jeuData.getPlateau());
        }

        return bestMove == null ? pastSequence : bestMove;
    }

    private Couleur oppose(Couleur couleur) {
        return couleur == Couleur.BLANC ? Couleur.NOIR : Couleur.BLANC;
    }

    private class MoveSequence {
        private final List<Mouvement> mouvements;
        private final int value;

        MoveSequence() {
            this.value = 0;
            this.mouvements = new ArrayList<>();

        }

        private MoveSequence(List<Mouvement> mouvements, int value) {
            this.mouvements = mouvements;
            this.value = value;
        }

        MoveSequence addAndReturn(Mouvement mouvement) {
            List<Mouvement> newList = new ArrayList<>(mouvements);
            newList.add(mouvement);
            return new MoveSequence(newList, value + mouvement.getValeur());
        }

        int getValue() {
            return value;
        }

        int getLength() {
            return mouvements.size();
        }

        Mouvement getFirst() {
            return mouvements.get(0);
        }
    }

    @Override
    String getNom() {
        return "Ordinateur";
    }
}