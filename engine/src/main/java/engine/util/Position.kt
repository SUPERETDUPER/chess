package engine.util

import java.io.Serializable

/**
 * A position (square) on the pieceMap. Top-left is (0,0)
 *
 * Is also used as a relative position (a shift/Position)
 */
data class Position(val row: Int, val column: Int) : Serializable {

    /**
     * @return if the position is within the limits (is a valid position on the pieceMap)
     */
    val isValid: Boolean
        get() = row in 0 until LIMIT && column in 0 until LIMIT

    /**
     * @param Position how much to add to this position
     * @return a new position that is shifted from this
     */
    operator fun plus(Position: Position): Position {
        return Position(row + Position.row, column + Position.column)
    }

    operator fun minus(Position: Position): Position {
        return Position(row - Position.row, column - Position.column)
    }
}

const val LIMIT = 8
val SHIFT_TOP_LEFT = Position(-1, -1)
val SHIFT_UP = Position(-1, 0)
val SHIFT_TOP_RIGHT = Position(-1, 1)
val SHIFT_LEFT = Position(0, -1)
val SHIFT_RIGHT = Position(0, 1)
val SHIFT_BOTTOM_LEFT = Position(1, -1)
val SHIFT_DOWN = Position(1, 0)
val SHIFT_BOTTOM_RIGHT = Position(1, 1)