package engine.util

/**
 * Iterator that loops through all the position on the pieceMap
 */
class PositionIterator : Iterator<Position> {
    private val maximum = Position(LIMIT, 0) //One row below max

    private var nextPosition = Position(0, 0)

    override fun hasNext(): Boolean {
        return nextPosition != maximum
    }

    override fun next(): Position {
        val currentPosition = nextPosition

        nextPosition += SHIFT_RIGHT

        if (nextPosition.column == LIMIT) nextPosition = Position(nextPosition.row + 1, 0)

        return currentPosition
    }
}
