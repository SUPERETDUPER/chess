package modele.moves;

import modele.board.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class NormalMoveTest {

    @Test
    void equals() {
        Assertions.assertEquals(
                new NormalMove(new Position(0, 0), new Position(0, 1)),
                new NormalMove(new Position(0, 0), new Position(0, 1))
        );
    }
}