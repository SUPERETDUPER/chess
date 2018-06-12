package model.moves;

import model.pieces.King;
import model.util.Colour;
import model.util.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MoveBougerTest {

    @Test
    void equals() {
        King piece = new King(Colour.BLANC);
        Assertions.assertEquals(
                new BaseMove(new Position(0, 0), new Position(0, 1)),
                new BaseMove(new Position(0, 0), new Position(0, 1))
        );
    }
}