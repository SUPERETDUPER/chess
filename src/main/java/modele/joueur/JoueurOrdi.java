package modele.joueur;

import gui.jeu.board.Board;
import modele.JeuData;
import modele.moves.Mouvement;
import modele.pieces.Couleur;

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
    public enum Difficulte {
        DIFFICILE {
            @Override
            public String toString() {
                return "difficile";
            }
        },
        FACILE {
            @Override
            public String toString() {
                return "facile";
            }
        }
    }

    private final int depth;
    private final Difficulte difficulte;

    private JeuData jeuData;

    public JoueurOrdi(Difficulte difficulte) {
        this.difficulte = difficulte;

        switch (difficulte) {
            case FACILE:
                depth = 3;
                break;
            case DIFFICILE:
                depth = 4;
                break;
            default:
                throw new RuntimeException("Difficulté inconnue");
        }
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
        new Thread(() -> {
            Resultat resultat = calculerMeilleurMouvement(new MoveSequence(), couleur);
            System.out.println(resultat.getMouvementsAnalyzer());
            callback.accept(resultat.getMoveSequence().getFirst());
        }).start();
    }

    private Resultat calculerMeilleurMouvement(MoveSequence pastSequence, Couleur couleur) {
        if (pastSequence.getLength() == depth) return new Resultat(pastSequence, 1);

        Set<Mouvement> mouvements;

        if (pastSequence.getLength() == 0) {
            mouvements = jeuData.getAllLegalMoves(couleur);
        } else {
            mouvements = jeuData.getAllMoves(couleur);
        }

        MoveSequence bestMove = null;
        int mouvementsAnalyzer = 0;

        for (Mouvement mouvement : mouvements) {
            mouvement.appliquer(jeuData.getPlateau());
            Resultat resultat = calculerMeilleurMouvement(pastSequence.addAndReturn(mouvement), oppose(couleur));
            MoveSequence moveSequence = resultat.getMoveSequence();
            mouvementsAnalyzer += resultat.getMouvementsAnalyzer();

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

        return new Resultat(bestMove == null ? pastSequence : bestMove, mouvementsAnalyzer);
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

    private class Resultat {
        private final MoveSequence moveSequence;
        private final int mouvementsAnalyzer;

        public Resultat(MoveSequence moveSequence, int mouvementsAnalyzer) {
            this.moveSequence = moveSequence;
            this.mouvementsAnalyzer = mouvementsAnalyzer;
        }

        public int getMouvementsAnalyzer() {
            return mouvementsAnalyzer;
        }

        public MoveSequence getMoveSequence() {
            return moveSequence;
        }
    }

    @Override
    String getNom() {
        return "Ordinateur (" + difficulte + ")";
    }
}