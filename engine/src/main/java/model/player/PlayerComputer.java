package model.player;

import model.GameData;
import model.moves.Move;
import model.util.Colour;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.function.Consumer;

//TODO Upgrade algorithm to min/max with alpha-beta pruning
//TODO Write tests because behaves weird
//TODO consider moving pieces values (ex. pawn = 1) to this class since pieces should not be responsible for this algorithm

/**
 * This player uses an algorithm to find the best next move.
 * The algorithm is a recursive min-max algorithm that uses the relative value of each move to evaluate the best move
 * <p>
 * There are two difficulty levels (Easy and Hard) they change the search depth of the algorithm
 */
public class PlayerComputer extends Player {
    //The difficulty levels
    public static final Difficulty EASY = new Difficulty(3, "Easy");
    public static final Difficulty HARD = new Difficulty(4, "Hard");

    /**
     * The difficulty level of the current instance of this player
     */
    private final Difficulty difficulty;

    /**
     * The game data
     */
    @Nullable
    private GameData gameData;

    /**
     * @param difficulty the difficulty level for the algorithm
     */
    public PlayerComputer(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public void initializeGameData(@NotNull GameData gameData) {
        this.gameData = gameData;
    }

    /**
     * Calculates the best move and returns it via the callback.
     */
    @Override
    public void getMove(@NotNull Consumer<Move> callback, Colour colour) {
        new Thread(() ->
                callback.accept(
                        calculateBestMove(new MoveSequence(), colour)
                                .getFirstMove()
                )
        ).start();
    }

    @NotNull
    private MoveSequence calculateBestMove(MoveSequence pastSequence, Colour colour) {
        //If we've reached the max depth return the current sequence
        if (pastSequence.getLength() == difficulty.searchDepth) return pastSequence;

        //Calculate all the possible moves
        Collection<Move> possibleMoves = gameData.getPossibleLegalMoves(colour);

        //The best move
        MoveSequence bestMove = null;

        for (Move move : possibleMoves) {
            move.apply(gameData); //Apply the move to the data

            //Calculate the value of this move (using recursion)
            MoveSequence moveSequence = calculateBestMove(new MoveSequence(pastSequence, move), getOppositeColour(colour));

            //If the move is better than bestMove update bestMove.
            //If the move is equal to bestMove update bestMove 50% of the time (to allow for variation)
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

            move.undo(gameData); //Undo changes
        }

        return bestMove == null ? pastSequence : bestMove;
    }

    /**
     * @return the opposite colour (used to switch turns)
     */
    @NotNull
    private Colour getOppositeColour(Colour colour) {
        return colour == Colour.WHITE ? Colour.BLACK : Colour.WHITE;
    }

    /**
     * An object representing a series of moves and its value
     */
    private class MoveSequence {
        @NotNull
        private final LinkedList<Move> moves;

        /**
         * The sum of the value of each move
         */
        private final int sequenceValue;

        MoveSequence() {
            this.sequenceValue = 0;
            this.moves = new LinkedList<>();
        }

        private MoveSequence(MoveSequence moveSequence, @NotNull Move move) {
            this.moves = new LinkedList<>(moveSequence.moves);
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
            return moves.getFirst();
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();

            moves.forEach(obj -> stringBuilder.append(obj).append(" "));

            return stringBuilder.toString();
        }
    }

    /**
     * The difficulty of the algorithm. Has a search depth and a name
     */
    private static class Difficulty implements Serializable {
        private final int searchDepth;
        private final String name;

        Difficulty(int searchDepth, String name) {
            this.searchDepth = searchDepth;
            this.name = name;
        }
    }

    @NotNull
    @Override
    public String getName() {
        return "Computer (" + difficulty.name + ")";
    }
}