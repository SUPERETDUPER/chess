package model.pieces

import model.GameData
import model.moves.BaseMove
import model.moves.Move
import model.util.BoardMap
import model.util.Colour
import model.util.Position
import java.io.Serializable
import java.util.*

/**
 * A piece in the game
 */
abstract class Piece internal constructor(
        /**
         * @return the pieces colour (white or black)
         */
        val colour: Colour) : Serializable {

    /**
     * @return the unicode for the piece to allow it to be displayed as text
     */
    val unicode: Int
        get() = if (colour == Colour.WHITE) unicodeWhite else unicodeBlack

    /**
     * @return the value of the piece depending on its color. White is positive, black is negative
     */
    val signedValue: Int
        get() = if (colour == Colour.WHITE) unsignedValue else -unsignedValue

    /**
     * @return the unicode for the white version of the piece
     */
    internal abstract val unicodeWhite: Int

    /**
     * @return the unicode for the black version of the piece
     */
    internal abstract val unicodeBlack: Int

    /**
     * @return the positive value of this piece
     */
    abstract val unsignedValue: Int

    internal abstract val name: String

    /**
     * Does not check for checks (king being attacked)
     *
     * @param start where the piece is at right now
     * @return the list of positions where the piece can move
     */
    internal abstract fun generatePossibleDestinations(gameData: GameData, start: Position): Collection<Position>

    /**
     * Separate method to allow overriding from subclasses if a special move is required
     *
     * @return a move that will move the piece from its current position to its destination
     */
    internal open fun convertDestinationToMove(board: BoardMap, current: Position, destination: Position): Move {
        return BaseMove(current, destination)
    }

    /**
     * Does not verify if move is legal (if king is put in check)
     *
     * @return a collection of all the moves that can be executed
     */
    fun generatePossibleMoves(gameData: GameData, start: Position): Collection<Move> {
        val moves = LinkedList<Move>()

        //For each possible destination create a move and add it to the list
        generatePossibleDestinations(gameData, start).forEach { destination -> moves.add(convertDestinationToMove(gameData.board, start, destination)) }

        return moves
    }

    open fun isAttackingPosition(gameData: GameData, position: Position): Boolean {
        val positions = generatePossibleDestinations(gameData, gameData.board.getPosition(this)!!)
        return positions.contains(position)
    }

    /**
     * Called when a move is applied to this piece
     *
     * @param move the move that was applied
     */
    internal abstract fun notifyMoveComplete(move: Move)

    /**
     * Called when a move is undone on this piece
     *
     * @param move the move that was undone
     */
    internal abstract fun notifyMoveUndo(move: Move)

    override fun toString(): String {
        return name + "-" + if (colour == Colour.WHITE) "w" else "b"
    }
}
