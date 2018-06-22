package engine.moves

import engine.GameData
import engine.pieces.Piece
import engine.util.Position
import org.jetbrains.annotations.Contract
import java.io.Serializable
import java.util.*

/**
 * A move. Each move has a start and end location. Each move has a piece (defined only when move is applied)
 */
abstract class Move protected constructor(val start: Position, val end: Position) : Serializable {
    /**
     * The piece that is moving. Gets set in method applyToGame
     */
    var piece: Piece? = null
        protected set

    /**
     * @return the value of the move (difference in board value)
     */
    abstract val value: Int

    /**
     * Applies this move to the game data
     */
    fun apply(data: GameData) {
        applyToGame(data)
        data.pastMoves.add(this)
        piece!!.notifyMoveComplete(this)
    }

    /**
     * Implemented by subclasses to be actually applied to game data
     */
    internal abstract fun applyToGame(data: GameData)

    /**
     * Undoes the move from the game data
     */
    fun undo(data: GameData) {
        data.pastMoves.removeLast()
        undoToGame(data)
        piece!!.notifyMoveUndo(this)
    }

    internal abstract fun undoToGame(data: GameData)

    /**
     * @return true if the obj is a move with the same start and end
     */
    @Contract(value = "null -> false", pure = true)
    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other !is Move) return false
        return if (this.start != other.start) false else this.end == other.end
    }

    override fun hashCode(): Int {
        return Objects.hash(this.piece, this.end)
    }

    override fun toString(): String {
        return start.toString() + " to " + end
    }
}
