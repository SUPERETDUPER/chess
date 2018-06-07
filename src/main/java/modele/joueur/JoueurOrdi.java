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
    public static final Difficulte NIVEAU_FACILE = new Difficulte(3, 20000, "Facile");
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
        new Thread(() -> callback.accept(new Calculateur().calculerMeilleurMouvement(couleur))).start();
    }

    private class Calculateur {
        private int mouvementsAnalyzer = 0;

        private Mouvement calculerMeilleurMouvement(Couleur couleur) {
            State bestState = null;

            Queue<State> states = new LinkedList<>(Collections.singletonList(new State(couleur)));

            while (true) {
                State state = states.remove(); //Obtenir la prochaine position à analyzer

                for (Mouvement mouvement : state.getPreviousMoves()) {
                    mouvement.appliquer(jeuData.getPlateau());
                }

                if (state.previousMoves.size() != 0) {
                    if (bestState == null) {
                        bestState = state;
                    } else {
                        if (couleur == Couleur.BLANC) {
                            //50% du temps si le mouvement à la même valeur échanger pour avoir de la variété
                            if (state.getValue() > bestState.getValue()
                                    || (state.getValue() == bestState.getValue() && Math.random() > 0.5)) {
                                bestState = state;
                            }
                        } else {
                            //50% du temps si le mouvement à la même valeur échanger pour avoir de la variété
                            if ((state.getValue() < bestState.getValue())
                                    || (state.getValue() == bestState.getValue() && Math.random() > 0.5)) {
                                bestState = state;
                            }
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
                    mouvements = jeuData.getAllLegalMoves(state.getProchainJoueur());
                } else {
                    mouvements = jeuData.getAllMoves(state.getProchainJoueur());
                }

                for (Mouvement mouvement : mouvements) {
                    System.out.println("analysing: " + mouvement);
                    mouvement.appliquer(jeuData.getPlateau());
                    State prochainState = new State(state, mouvement);
                    states.add(new State(prochainState, getBestMove(prochainState)));
                    mouvement.undo(jeuData.getPlateau());
                }

                for (int i = state.getPreviousMoves().size() - 1; i >= 0; i--) {
                    state.getPreviousMoves().get(i).undo(jeuData.getPlateau());
                }
            }

            System.out.println(mouvementsAnalyzer + " " + bestState + " ma couleur " + couleur);

            return bestState.getPreviousMoves().get(0);
        }

        private Mouvement getBestMove(State state) {
            Set<Mouvement> mouvements;

            if (state.getPreviousMoves().size() == 0) {
                mouvements = jeuData.getAllLegalMoves(state.getProchainJoueur());
            } else {
                mouvements = jeuData.getAllMoves(state.getProchainJoueur());
            }

            Mouvement best = null;

            for (Mouvement mouvement : mouvements) {
                if (best == null) best = mouvement;
                else {
                    if (state.getProchainJoueur() == Couleur.BLANC) {
                        //50% du temps si le mouvement à la même valeur échanger pour avoir de la variété
                        if (mouvement.getValeur() > best.getValeur()
                                || (mouvement.getValeur() == best.getValeur() && Math.random() > 0.5)) {
                            best = mouvement;
                        }
                    } else {
                        //50% du temps si le mouvement à la même valeur échanger pour avoir de la variété
                        if ((mouvement.getValeur() < best.getValeur())
                                || (mouvement.getValeur() == best.getValeur() && Math.random() > 0.5)) {
                            best = mouvement;
                        }
                    }
                }
                mouvementsAnalyzer += 1;
            }

            return best;
        }

        private class State {
            private final int value;
            private final List<Mouvement> previousMoves;
            private final Couleur prochainJoueur;

            State(State previousState, Mouvement mouvement) {
                this.value = previousState.value + mouvement.getValeur();
                this.prochainJoueur = previousState.prochainJoueur == Couleur.BLANC ? Couleur.NOIR : Couleur.BLANC;
                this.previousMoves = new ArrayList<>(previousState.previousMoves);
                previousMoves.add(mouvement);
            }

            State(Couleur prochainJoueur) {
                this.prochainJoueur = prochainJoueur;
                this.previousMoves = new ArrayList<>();
                this.value = 0;
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

            Couleur getProchainJoueur() {
                return prochainJoueur;
            }

            @Override
            public String toString() {
                return "moves: " + previousMoves +
                        " value: " + value + " prochain joueur " + prochainJoueur;
            }
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