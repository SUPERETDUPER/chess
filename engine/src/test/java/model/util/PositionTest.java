package model.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PositionTest {

    /**
     * Checks that two position objects with the same value are equal
     */
    @Test
    void equals() {
        Assertions.assertEquals(new Position(0, 1), new Position(0, 1));
        Assertions.assertNotEquals(new Position(0, 0), new Position(0, 1));
    }
}