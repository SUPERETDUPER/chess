package modele.joueur;

import gui.jeu.board.PlateauPane;
import modele.Couleur;
import modele.JeuData;
import modele.moves.Mouvement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

//TODO Upgrade algorithm to min/max with alpha-beta pruning
//TODO Write tests because behaves weird

/**
 * Un joueur qui utilise un algorithm pour trouver son prochain mouvement
 */
public class JoueurOrdi extends Joueur {
    public static final Difficulte NIVEAU_FACILE = new Difficulte(3, "Facile");
    public static final Difficulte NIVEAU_DIFFICILE = new Difficulte(4, "Difficile");

    private Difficulte difficulte;

    private JeuData jeuData;

    public JoueurOrdi(Difficulte difficulte) {
        this.difficulte = difficulte;
    }

    @Override
    public void initializeJeuData(JeuData jeuData) {
        this.jeuData = jeuData;
    }

    @Override
    public void initializeInterface(PlateauPane plateauPane) {

    }

    /**
     * Trouve tous les mouvement possible puis retourne celui qui remporte le plus de points
     *
     * @param callback la méthode par laquelle l'on soumet son prochain mouvement
     */
    @Override
    public void getMouvement(Consumer<Mouvement> callback, Couleur couleur) {
        new Thread(() ->
                callback.accept(calculerMeilleurMouvement(new MoveSequence(), couleur).getFirst())
        ).start();

    }

    private MoveSequence calculerMeilleurMouvement(MoveSequence pastSequence, Couleur couleur) {
        if (pastSequence.getLength() == difficulte.depth) return pastSequence;

        Set<Mouvement> mouvements;

        if (pastSequence.getLength() == 0) {
            mouvements = jeuData.getAllLegalMoves(couleur);
        } else {
            mouvements = jeuData.plateau.getAllMoves(couleur, jeuData);
        }

        MoveSequence bestMove = null;

        for (Mouvement mouvement : mouvements) {
            mouvement.appliquer(jeuData.getPlateau());
            MoveSequence moveSequence = calculerMeilleurMouvement(pastSequence.addAndReturn(mouvement), oppose(couleur));

            if (bestMove == null) {
                bestMove = moveSequence;
            } else {
                if (couleur == Couleur.BLANC) {
                    //50% du temps si le mouvement à la même valeur échanger pour avoir de la variété
                    if (moveSequence.getValue() > bestMove.getValue()
                            || (moveSequence.getValue() == bestMove.getValue() && Math.random() > 0.5)) {
                        bestMove = moveSequence;
                    }
                } else {
                    //50% du temps si le mouvement à la même valeur échanger pour avoir de la variété
                    if (moveSequence.getValue() < bestMove.getValue()
                            || (moveSequence.getValue() == bestMove.getValue() && Math.random() > 0.5)) {
                        bestMove = moveSequence;
                    }
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

    private static class Difficulte implements Serializable {
        private final int depth;
        private final String nom;

        public Difficulte(int depth, String nom) {
            this.depth = depth;
            this.nom = nom;
        }
    }

    @Override
    String getNom() {
        return "Ordinateur (" + difficulte.nom + ")";
    }
}