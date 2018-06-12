package model.util;

import java.util.Iterator;

/**
 * Traverse toutes les positions sur le boardregion
 */
public class PositionIterator implements Iterator<Position> {
    private static final Position MAX = new Position(Position.LIMITE, 0);
    private static final Offset ADROITE = new Offset(0, 1);

    private Position position = new Position(0, 0);

    @Override
    public boolean hasNext() {
        return !position.equals(MAX);
    }

    @Override
    public Position next() {
        Position positionToReturn = position;

        if (position.getColonne() == Position.LIMITE - 1) {
            position = new Position(position.getRangee() + 1, 0);
        } else {
            position = position.decaler(ADROITE);
        }

        return positionToReturn;
    }
}
