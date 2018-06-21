package model.player

import model.GameData
import model.moves.Move
import model.util.Colour
import java.io.Serializable
import java.util.*
import java.util.function.Consumer

//TODO Upgrade algorithm to min/max with alpha-beta pruning
//TODO Write tests because behaves weird
//TODO consider moving pieces values (ex. pawn = 1) to this class since pieces should not be responsible for this algorithm

/**
 * This player uses an algorithm to find the best next move.
 * The algorithm is a recursive min-max algorithm that uses the relative value of each move to evaluate the best move
 *
 *
 * There are two difficulty levels (Easy and Hard) they change the search depth of the algorithm
 */
class PlayerComputer
/**
 * @param difficulty the difficulty level for the algorithm
 */
(
        /**
         * The difficulty level of the current instance of this player
         */
        private val difficulty: Difficulty) : Player() {

    /**
     * The game data
     */
    private var gameData: GameData? = null

    override val name: String = "Computer (" + difficulty.name + ")"

    override fun initializeGameData(gameData: GameData) {
        this.gameData = gameData
    }

    /**
     * Calculates the best move and returns it via the callback.
     */
    override fun getMove(callback: Consumer<Move>, colour: Colour) {
        Thread {
            callback.accept(
                    calculateBestMove(MoveSequence(), colour)
                            .firstMove
            )
        }.start()
    }

    private fun calculateBestMove(pastSequence: MoveSequence, colour: Colour): MoveSequence {
        //If we've reached the max depth return the current sequence
        if (pastSequence.length == difficulty.searchDepth) return pastSequence

        //Calculate all the possible moves
        val possibleMoves = gameData!!.getPossibleLegalMoves(colour)

        //The best move
        var bestMove: MoveSequence? = null

        for (move in possibleMoves) {
            move.apply(gameData!!) //Apply the move to the data

            //Calculate the value of this move (using recursion)
            val moveSequence = calculateBestMove(MoveSequence(pastSequence, move), getOppositeColour(colour))

            //If the move is better than bestMove update bestMove.
            //If the move is equal to bestMove update bestMove 50% of the time (to allow for variation)
            if (bestMove == null) {
                bestMove = moveSequence
            } else {
                if (colour == Colour.WHITE) {
                    if (moveSequence.sequenceValue > bestMove.sequenceValue || moveSequence.sequenceValue == bestMove.sequenceValue && Math.random() > 0.5) {
                        bestMove = moveSequence
                    }
                } else {
                    if (moveSequence.sequenceValue < bestMove.sequenceValue || moveSequence.sequenceValue == bestMove.sequenceValue && Math.random() > 0.5) {
                        bestMove = moveSequence
                    }
                }
            }

            move.undo(gameData!!) //Undo changes
        }

        return if (bestMove == null) pastSequence else bestMove
    }

    /**
     * @return the opposite colour (used to switch turns)
     */
    private fun getOppositeColour(colour: Colour): Colour {
        return if (colour == Colour.WHITE) Colour.BLACK else Colour.WHITE
    }

    /**
     * An object representing a series of moves and its value
     */
    private inner class MoveSequence {
        private val moves: LinkedList<Move>

        /**
         * The sum of the value of each move
         */
        internal val sequenceValue: Int

        internal val length: Int
            get() = moves.size

        internal val firstMove: Move
            get() = moves.first

        internal constructor() {
            this.sequenceValue = 0
            this.moves = LinkedList()
        }

        internal constructor(moveSequence: MoveSequence, move: Move) {
            this.moves = LinkedList(moveSequence.moves)
            this.moves.add(move)
            this.sequenceValue = moveSequence.sequenceValue + move.value
        }

        override fun toString(): String {
            val stringBuilder = StringBuilder()

            moves.forEach { obj -> stringBuilder.append(obj).append(" ") }

            return stringBuilder.toString()
        }
    }

    /**
     * The difficulty of the algorithm. Has a search depth and a name
     */
    class Difficulty internal constructor(internal val searchDepth: Int, internal val name: String) : Serializable

    companion object {
        //The difficulty levels
        val EASY = Difficulty(3, "Easy")
        val HARD = Difficulty(4, "Hard")
    }
}