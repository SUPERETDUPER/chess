package engine.moves

import engine.util.Position
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class BaseMoveTest {

    @Test
    fun equals() {
        Assertions.assertEquals(
                BaseMove(Position(0, 0), Position(0, 1)),
                BaseMove(Position(0, 0), Position(0, 1))
        )
    }
}