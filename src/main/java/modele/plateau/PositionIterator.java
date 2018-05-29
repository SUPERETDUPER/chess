package modele.plateau;

import java.util.Iterator;

public class PositionIterator implements Iterator<Position> {
    private final static Position MAX = new Position(Position.getLimite(), 0);

    private Position position = new Position(0, 0);

    @Override
    public boolean hasNext() {
        return !position.equals(MAX);
    }

    @Override
    public Position next() {
        Position positionToReturn = position;

        if (position.getColonne() == Position.getLimite() - 1) {
            position = new Position(position.getRangee() + 1, 0);
        } else {
            position = position.offset(0, 1);
        }

        return positionToReturn;
    }
}
