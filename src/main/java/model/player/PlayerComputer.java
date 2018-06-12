package model.player;

import model.GameData;
import model.moves.Move;
import model.util.Colour;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

//TODO Upgrade algorithm to min/max with alpha-beta pruning
//TODO Write tests because behaves weird
//TODO consider moving pieces values (ex. pawn = 1) to this class since pieces should not be responsible for the algorithm

/**
 * Un player qui utilise un algorithm pour trouver le prochain meilleur moves.
 * L'algorithme est un algorithm recursif min-max qui utilise les valeurs des pièces (chaque pièce à une sequenceValue)
 *
 * Il existe deux niveaux de difficulté (facile et difficile)
 */
public class PlayerComputer extends Player {
    //Les niveaux de difficultés
    public static final Difficulty EASY = new Difficulty(3, "Facile");
    public static final Difficulty HARD = new Difficulty(4, "Difficile");

    /**
     * Le niveau de difficulty du player
     */
    private final Difficulty difficulty;

    /**
     * Le gamewindow data
     */
    @Nullable
    private GameData gameData;

    /**
     * @param difficulty la difficulté de l'algorithme
     */
    public PlayerComputer(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public void initializeGameData(@NotNull GameData gameData) {
        this.gameData = gameData;
    }

    /**
     * Retourne le moves qui retourne le plus de points
     *
     * @param callback la méthode par laquelle l'on soumet son prochain moves
     */
    @Override
    public void getMove(Consumer<Move> callback, Colour colour) {
        new Thread(() ->
                callback.accept(
                        calculateBestMove(new MoveSequence(), colour)
                                .getFirstMove()
                )
        ).start();
    }

    @NotNull
    private MoveSequence calculateBestMove(MoveSequence pastSequence, Colour colour) {
        //Si on est déjà à la profondeur maximale retourner
        if (pastSequence.getLength() == difficulty.searchDepth) return pastSequence;

        //Calculer les moves possibles
        Collection<Move> possibleMoves = gameData.getAllLegalMoves(colour);

        MoveSequence bestMove = null;

        for (Move move : possibleMoves) {
            move.apply(gameData); //Appliquer le moves

            //Calculer la sequenceValue du moves (partie récursive)
            MoveSequence moveSequence = calculateBestMove(new MoveSequence(pastSequence, move), getOppositeColour(colour));

            //Si le moves est meilleur que bestMove remplacer bestMove
            //Si le moves à la même sequenceValue, remplacer 50% du temps
            if (bestMove == null) {
                bestMove = moveSequence;
            } else {
                if (colour == Colour.WHITE) {
                    if (moveSequence.getSequenceValue() > bestMove.getSequenceValue()
                            || (moveSequence.getSequenceValue() == bestMove.getSequenceValue() && Math.random() > 0.5)) {
                        bestMove = moveSequence;
                    }
                } else {
                    if (moveSequence.getSequenceValue() < bestMove.getSequenceValue()
                            || (moveSequence.getSequenceValue() == bestMove.getSequenceValue() && Math.random() > 0.5)) {
                        bestMove = moveSequence;
                    }
                }
            }

            move.undo(gameData); //Défaire les changements
        }

        return bestMove == null ? pastSequence : bestMove;
    }

    /**
     * Si blanc retourne noir et vice-versa
     *
     * @param colour la colour à getOppositeColour
     * @return la colour inversée
     */
    private Colour getOppositeColour(Colour colour) {
        return colour == Colour.WHITE ? Colour.BLACK : Colour.WHITE;
    }

    /**
     * Une classe qui représente une série de moves et la sequenceValue de la série
     */
    private class MoveSequence {
        private final List<Move> moves;

        /**
         * La somme de la sequenceValue de toutes les pièces
         */
        private final int sequenceValue;

        MoveSequence() {
            this.sequenceValue = 0;
            this.moves = new ArrayList<>();
        }

        private MoveSequence(MoveSequence moveSequence, Move move) {
            this.moves = new ArrayList<>(moveSequence.moves);
            this.moves.add(move);
            this.sequenceValue = moveSequence.sequenceValue + move.getValue();
        }

        int getSequenceValue() {
            return sequenceValue;
        }

        int getLength() {
            return moves.size();
        }

        Move getFirstMove() {
            return moves.get(0);
        }
    }

    /**
     * La difficulté de l'algorithm (dépendant de la profondeur)
     */
    private static class Difficulty implements Serializable {
        private final int searchDepth;
        private final String name;

        Difficulty(int profondeur, String name) {
            this.searchDepth = profondeur;
            this.name = name;
        }
    }

    @Override
    public String getName() {
        return "Ordinateur (" + difficulty.name + ")";
    }
}