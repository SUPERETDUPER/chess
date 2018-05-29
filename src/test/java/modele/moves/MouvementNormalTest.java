package modele.moves;

import modele.plateau.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MouvementNormalTest {

    @Test
    void equals() {
        Assertions.assertEquals(
                new MouvementNormal(new Position(0, 0), new Position(0, 1)),
                new MouvementNormal(new Position(0, 0), new Position(0, 1))
        );
    }
}