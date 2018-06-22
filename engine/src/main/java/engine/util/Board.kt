package engine.util


import org.jetbrains.annotations.Contract
import java.util.*

/**
 * A data structure that stores an object in each position (square) of the board.
 * These objects are accessed using a [Position] key.
 *
 *
 * Internally the objects are stored in a list
 *
 * @param <T> The object that is stored in the data structure
</T> */
class Board<T> : Iterable<T> {
    /**
     * The data
     */
    val data = ArrayList<T>(Position.LIMIT * Position.LIMIT)

    /**
     * @param position the position key
     * @return the object at this position
     */
    operator fun get(position: Position): T {
        return data[getIndex(position)]
    }

    fun add(position: Position, value: T) {
        data.add(getIndex(position), value)
    }

    /**
     * Calculates the index of an item at this position
     */
    @Contract(pure = true)
    private fun getIndex(position: Position): Int {
        return Position.LIMIT * position.row + position.column
    }

    /**
     * @return an iterator that loops through all the objects in the data structure. Uses the position iterator.
     */
    override fun iterator(): Iterator<T> {
        return object : Iterator<T> {
            private val positionIterator = PositionIterator()

            override fun hasNext(): Boolean {
                return positionIterator.hasNext()
            }

            override fun next(): T {
                return this@Board[positionIterator.next()]
            }
        }
    }
}