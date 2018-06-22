package model.moves;

import model.util.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BaseMoveTest {

    @Test
    void equals() {
        Assertions.assertEquals(
                new BaseMove(new Position(0, 0), new Position(0, 1)),
                new BaseMove(new Position(0, 0), new Position(0, 1))
        );
    }
}