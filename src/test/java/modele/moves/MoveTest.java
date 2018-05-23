package modele.moves;

import modele.board.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MoveTest {

    @Test
    void equals() {
        Assertions.assertEquals(
                new Move(new Position(0, 0), new Position(0, 1)),
                new Move(new Position(0, 0), new Position(0, 1))
        );
    }
}