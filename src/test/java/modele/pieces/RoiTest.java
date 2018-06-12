package modele.pieces;

import modele.mouvement.Mouvement;
import modele.mouvement.MouvementNormal;
import modele.util.Couleur;
import modele.util.Plateau;
import modele.util.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.LinkedList;

class RoiTest {

    @Test
    void generateMoves() {
        Roi roi = new Roi(Couleur.BLANC);

        Plateau plateau = new Plateau();
        Position startingPos = new Position(0, 0);
        plateau.ajouter(startingPos, roi);

        Collection<Mouvement> mouvements = roi.generateAllMoves(plateau, startingPos);

        Collection<Mouvement> expected = new LinkedList<>();
        expected.add(new MouvementNormal(startingPos, new Position(0, 1)));
        expected.add(new MouvementNormal(startingPos, new Position(1, 0)));
        expected.add(new MouvementNormal(startingPos, new Position(1, 1)));

        Assertions.assertEquals(expected, mouvements);
    }
}