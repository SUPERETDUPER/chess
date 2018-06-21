package model.util

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class PositionTest {

    /**
     * Checks that two position objects with the same value are equal
     */
    @Test
    fun equals() {
        Assertions.assertEquals(Position(0, 1), Position(0, 1))
        Assertions.assertNotEquals(Position(0, 0), Position(0, 1))
    }
}