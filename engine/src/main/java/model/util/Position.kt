package model.util

import java.io.Serializable
import java.util.*

/**
 * A position (square) on the board. Top-left is (0,0)
 */
data class Position(val row: Int, val column: Int) : Serializable {

    /**
     * @return if the position is within the limits (is a valid position on the board)
     */
    val isValid: Boolean
        get() = row in 0..(LIMIT - 1) && column in 0..(LIMIT-1)

    /**
     * @param offset how much to shift this position
     * @return a new position that is shifted from this
     */
    fun shift(offset: Offset): Position {
        return Position(row + offset.verticalOffset, column + offset.horizontalOffset)
    }

    companion object {
        const val LIMIT = 8
    }
}
