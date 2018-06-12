package model.util;

import java.util.Iterator;

/**
 * Traverse toutes les positions sur le boardregion
 */
public class PositionIterator implements Iterator<Position> {
    private static final Position MAXIMUM = new Position(Position.LIMITE, 0);

    private Position position = new Position(0, 0);

    @Override
    public boolean hasNext() {
        return !position.equals(MAXIMUM);
    }

    @Override
    public Position next() {
        Position positionToReturn = position;

        if (position.getColumn() == Position.LIMITE - 1) {
            position = new Position(position.getRow() + 1, 0);
        } else {
            position = position.shift(Offset.RIGHT);
        }

        return positionToReturn;
    }
}
