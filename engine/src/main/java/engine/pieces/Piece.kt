package engine.pieces

import engine.GameData
import engine.moves.BaseMove
import engine.moves.Move
import engine.util.PieceMap
import engine.util.Colour
import engine.util.Position
import java.io.Serializable
import java.util.*

/**
 * A pieceMap in the game
 *
 * @property colour the pieces colour (white or black)
 */
abstract class Piece internal constructor(val colour: Colour) : Serializable {

    /**
     * @return the unicode for the pieceMap to allow it to be displayed as text
     */
    val unicode: Int
        get() = if (colour == Colour.WHITE) unicodeWhite else unicodeBlack

    /**
     * @return the value of the pieceMap depending on its color. White is positive, black is negative
     */
    val signedValue: Int
        get() = if (colour == Colour.WHITE) unsignedValue else -unsignedValue

    /**
     * @return the unicode for the white version of the pieceMap
     */
    internal abstract val unicodeWhite: Int

    /**
     * @return the unicode for the black version of the pieceMap
     */
    internal abstract val unicodeBlack: Int

    /**
     * @return the positive value of this pieceMap
     */
    abstract val unsignedValue: Int

    internal abstract val name: String

    /**
     * Does not check for checks (king being attacked)
     *
     * @param start where the pieceMap is at right now
     * @return the list of positions where the pieceMap can move
     */
    internal abstract fun generatePossibleDestinations(gameData: GameData, start: Position): Collection<Position>

    /**
     * Separate method to allow overriding from subclasses if a special move is required
     *
     * @return a move that will move the pieceMap from its current position to its destination
     */
    internal open fun convertDestinationToMove(piece: PieceMap, current: Position, destination: Position): Move {
        return BaseMove(current, destination)
    }

    /**
     * Does not verify if move is legal (if king is put in check)
     *
     * @return a collection of all the moves that can be executed
     */
    fun generatePossibleMoves(gameData: GameData, start: Position): Collection<Move> {
        val moves = LinkedList<Move>()

        //For each possible destination create a move and put it to the list
        generatePossibleDestinations(gameData, start).forEach { destination -> moves.add(convertDestinationToMove(gameData.pieceMap, start, destination)) }

        return moves
    }

    open fun isAttackingPosition(gameData: GameData, attackingPosition: Position, attackersPosition: Position): Boolean {
        return generatePossibleDestinations(gameData, attackersPosition).contains(attackingPosition)
    }

    /**
     * Called when a move is applied to this pieceMap
     *
     * @param move the move that was applied
     */
    internal abstract fun notifyMoveComplete(move: Move)

    /**
     * Called when a move is undone on this pieceMap
     *
     * @param move the move that was undone
     */
    internal abstract fun notifyMoveUndo(move: Move)

    override fun toString(): String {
        return "$name-$colour"
    }
}
