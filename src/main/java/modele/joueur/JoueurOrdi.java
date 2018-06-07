package modele.joueur;

import gui.jeu.board.Board;
import modele.JeuData;
import modele.moves.Mouvement;
import modele.pieces.Couleur;

import java.util.*;
import java.util.function.Consumer;

//TODO Upgrade algorithm to min/max with alpha-beta pruning
//TODO Write tests because behaves weird

/**
 * Un joueur qui utilise un algorithm pour trouver son prochain mouvement
 */
public class JoueurOrdi extends Joueur {
    public static final Difficulte NIVEAU_FACILE = new Difficulte(3, 10000, "Facile");
    public static final Difficulte NIVEAU_DIFFICILE = new Difficulte(4, 40000, "Difficile");

    private final Difficulte difficulte;

    private JeuData jeuData;

    public JoueurOrdi(Difficulte difficulte) {
        this.difficulte = difficulte;
    }

    @Override
    public void initialize(JeuData jeuData, Board board) {
        this.jeuData = jeuData;
    }

    /**
     * Trouve tous les mouvement possible puis retourne celui qui remporte le plus de points
     *
     * @param callback la méthode par laquelle l'on soumet son prochain mouvement
     */
    @Override
    public void getMouvement(Consumer<Mouvement> callback, Couleur couleur) {
        new Thread(() -> callback.accept(calculerMeilleurMouvement(couleur))).start();
    }

    private Mouvement calculerMeilleurMouvement(Couleur couleur) {
        State bestState = null;

        int mouvementsAnalyzer = 0;

        Queue<State> states = new LinkedList<>(Collections.singletonList(new State(couleur)));

        while (true) {
            State state = states.remove(); //Obtenir la prochaine position à analyzer

            for (Mouvement mouvement : state.getPreviousMoves()) {
                mouvement.appliquer(jeuData.getPlateau());
            }

            if (bestState == null) {
                bestState = state;
            } else {
                if (state.getCouleur() == Couleur.NOIR) {
                    //50% du temps si le mouvement à la même valeur échanger pour avoir de la variété
                    if ((state.getValue() > bestState.getValue())
                            || (state.getValue() == bestState.getValue() && Math.random() > 0.5)) {
                        bestState = state;
                    }
                } else {
                    //50% du temps si le mouvement à la même valeur échanger pour avoir de la variété
                    if (state.getValue() < bestState.getValue()
                            || (state.getValue() == bestState.getValue() && Math.random() > 0.5)) {
                        bestState = state;
                    }
                }
            }

            mouvementsAnalyzer += 1;

            System.out.println(state.getSize() + " " + mouvementsAnalyzer);

            if (state.getSize() >= difficulte.getMinSearchDepth() && mouvementsAnalyzer >= difficulte.getMinSearchMoves()) {
                for (int i = state.getPreviousMoves().size() - 1; i >= 0; i--) {
                    state.getPreviousMoves().get(i).undo(jeuData.getPlateau());
                }

                break; //Si passe arreter
            }

            //Ajouter les autres options à la liste
            Set<Mouvement> mouvements;

            if (state.getPreviousMoves().size() == 0) {
                mouvements = jeuData.getAllLegalMoves(state.getCouleur());
            } else {
                mouvements = jeuData.getAllMoves(state.getCouleur());
            }

            for (Mouvement mouvement : mouvements) {
                states.add(state.appendAndReturn(mouvement));
            }

            for (int i = state.getPreviousMoves().size() - 1; i >= 0; i--) {
                state.getPreviousMoves().get(i).undo(jeuData.getPlateau());
            }
        }

        System.out.println(mouvementsAnalyzer + " " + bestState.getValue());

        return bestState.getPreviousMoves().get(0);
    }

    private class State {
        private final int value;
        private final List<Mouvement> previousMoves;
        private final Couleur couleur;

        State(int value, List<Mouvement> previousMoves, Couleur couleur) {
            this.value = value;
            this.couleur = couleur;
            this.previousMoves = previousMoves;
        }

        State(Couleur couleur) {
            this.couleur = couleur;
            this.value = 0;
            this.previousMoves = new ArrayList<>();
        }

        State appendAndReturn(Mouvement mouvement) {
            List<Mouvement> newMoveSequence = new ArrayList<>(this.previousMoves);
            newMoveSequence.add(mouvement);
            return new State(value + mouvement.getValeur(),
                    newMoveSequence,
                    couleur == Couleur.BLANC ? Couleur.NOIR : Couleur.BLANC
            );
        }

        List<Mouvement> getPreviousMoves() {
            return previousMoves;
        }

        int getValue() {
            return value;
        }

        int getSize() {
            return previousMoves.size();
        }

        public Couleur getCouleur() {
            return couleur;
        }
    }

    private static class Difficulte {
        private final int minSearchDepth;
        private final int minSearchMoves;
        private final String nom;

        Difficulte(int minSearchDepth, int minSearchMoves, String nom) {
            this.minSearchDepth = minSearchDepth;
            this.minSearchMoves = minSearchMoves;
            this.nom = nom;
        }

        int getMinSearchDepth() {
            return minSearchDepth;
        }

        int getMinSearchMoves() {
            return minSearchMoves;
        }

        String getNom() {
            return nom;
        }
    }


    @Override
    String getNom() {
        return "Ordinateur (" + difficulte.getNom() + ")";
    }
}