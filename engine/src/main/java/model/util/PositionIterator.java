package model.util;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

/**
 * Iterator that loops through all the position on the board
 */
public class PositionIterator implements Iterator<Position> {
    private static final Position MAXIMUM = new Position(Position.LIMIT, 0);

    @NotNull
    private Position position = new Position(0, 0);

    @Override
    public boolean hasNext() {
        return !position.equals(MAXIMUM);
    }

    @NotNull
    @Override
    public Position next() {
        Position positionToReturn = position;

        if (position.getColumn() == Position.LIMIT - 1) {
            position = new Position(position.getRow() + 1, 0);
        } else {
            position = position.shift(Offset.RIGHT);
        }

        return positionToReturn;
    }
}
