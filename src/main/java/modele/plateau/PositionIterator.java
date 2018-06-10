package modele.plateau;

import java.util.Iterator;

public class PositionIterator implements Iterator<Position> {
    private static final Position MAX = new PositionBase(PositionBase.LIMITE, 0);
    private static final Offset ADROITE = new Offset(0, 1);

    private Position position = new PositionBase(0, 0);

    @Override
    public boolean hasNext() {
        return !position.equals(MAX);
    }

    @Override
    public Position next() {
        Position positionToReturn = position;

        if (position.getColonne() == PositionBase.LIMITE - 1) {
            position = new PositionBase(position.getRangee() + 1, 0);
        } else {
            position = position.decaler(ADROITE);
        }

        return positionToReturn;
    }
}
