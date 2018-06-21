package model.util

import java.io.Serializable
import java.util.*

/**
 * A position (square) on the board. Top-left is (0,0)
 */
class Position(val row: Int, val column: Int) : Serializable {

    /**
     * @return if the position is within the limits (is a valid position on the board)
     */
    val isValid: Boolean
        get() = row in 0..(LIMIT - 1) && 0 <= column && column < LIMIT

    /**
     * @param offset how much to shift this position
     * @return a new position that is shifted from this
     */
    fun shift(offset: Offset): Position {
        return Position(row + offset.verticalShift, column + offset.horizontalShift)
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other !is Position) return false
        return if (other.row != this.row) false else other.column == this.column
    }

    override fun toString(): String {
        return "r" + row + "c" + column
    }


    override fun hashCode(): Int {
        return Objects.hash(column, row)
    }

    companion object {
        const val LIMIT = 8
    }
}
