package modele.board;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    /**
     * Vérifie que deux positions avec la même valeur sont égales
     */
    @Test
    void equals() {
        Assertions.assertEquals(new Position(0,1), new Position(0,1));
        Assertions.assertNotEquals(new Position(0,0), new Position(0,1));
    }
}