package modele.moves;

import modele.Couleur;
import modele.pieces.Roi;
import modele.plateau.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MouvementNormalTest {

    @Test
    void equals() {
        Roi piece = new Roi(Couleur.BLANC);
        Assertions.assertEquals(
                new MouvementNormal(piece, new Position(0, 1)),
                new MouvementNormal(piece, new Position(0, 1))
        );
    }
}