package model.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PositionIteratorTest {
    @Test
    void test() {
        PositionIterator positionIterator = new PositionIterator();

        int i = 0;

        while (positionIterator.hasNext()) {
            Position position = positionIterator.next();

            if (i == 0) {
                Assertions.assertEquals(new Position(0, 0), position);
            } else if (i == 1) {
                Assertions.assertEquals(new Position(0, 1), position);
            } else if (i == Position.LIMITE * Position.LIMITE - 1) {
                Assertions.assertEquals(new Position(Position.LIMITE - 1, Position.LIMITE - 1), position);
            } else if (i >= Position.LIMITE * Position.LIMITE) {
                throw new RuntimeException("Position trop grande: " + position.toString());
            }

            i++;
        }
    }
}