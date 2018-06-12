package modele.mouvement;

import modele.pieces.Roi;
import modele.util.Couleur;
import modele.util.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MouvementBougerTest {

    @Test
    void equals() {
        Roi piece = new Roi(Couleur.BLANC);
        Assertions.assertEquals(
                new MouvementNormal(new Position(0, 0), new Position(0, 1)),
                new MouvementNormal(new Position(0, 0), new Position(0, 1))
        );
    }
}