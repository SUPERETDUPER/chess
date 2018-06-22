package engine.util

/**
 * Iterator that loops through all the position on the board
 */
class PositionIterator : Iterator<Position> {

    private var position = Position(0, 0)

    override fun hasNext(): Boolean {
        return position != MAXIMUM
    }

    override fun next(): Position {
        val positionToReturn = position

        position =
                if (position.column == Position.LIMIT - 1) {
                    Position(position.row + 1, 0)
                } else {
                    position.shift(Offset.RIGHT)
                }

        return positionToReturn
    }

    companion object {
        private val MAXIMUM = Position(Position.LIMIT, 0)
    }
}
