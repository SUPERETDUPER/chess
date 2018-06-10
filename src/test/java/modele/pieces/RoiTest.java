package modele.pieces;

import modele.Couleur;
import modele.moves.Mouvement;
import modele.moves.MouvementNormal;
import modele.plateau.PlateauPiece;
import modele.plateau.Position;
import modele.plateau.PositionBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

class RoiTest {

    @Test
    void generateMoves() {
        Roi roi = new Roi(Couleur.BLANC);

        PlateauPiece plateau = new PlateauPiece();
        Position startingPos = new PositionBase(0, 0);
        plateau.ajouterPiece(roi, startingPos);

        Set<Mouvement> mouvements = roi.generateAllMoves(plateau);

        Set<Mouvement> expected = new HashSet<>();
        expected.add(new MouvementNormal(roi, new PositionBase(0, 1)));
        expected.add(new MouvementNormal(roi, new PositionBase(1, 0)));
        expected.add(new MouvementNormal(roi, new PositionBase(1, 1)));

        Assertions.assertEquals(expected, mouvements);
    }
}