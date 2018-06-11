package modele.pieces;

import modele.mouvement.Mouvement;
import modele.mouvement.MouvementBouger;
import modele.util.Couleur;
import modele.util.Plateau;
import modele.util.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

class RoiTest {

    @Test
    void generateMoves() {
        Roi roi = new Roi(Couleur.BLANC);

        Plateau plateau = new Plateau();
        Position startingPos = new Position(0, 0);
        plateau.ajouter(startingPos, roi);

        Set<Mouvement> mouvements = roi.generateAllMoves(plateau);

        Set<Mouvement> expected = new HashSet<>();
        expected.add(new MouvementBouger(roi, new Position(0, 1)));
        expected.add(new MouvementBouger(roi, new Position(1, 0)));
        expected.add(new MouvementBouger(roi, new Position(1, 1)));

        Assertions.assertEquals(expected, mouvements);
    }
}