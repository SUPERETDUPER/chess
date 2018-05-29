package modele.pieces;

import modele.moves.Move;
import modele.moves.NormalMove;
import modele.plateau.Plateau;
import modele.plateau.Position;
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

        Set<Move> moves = roi.generateAllMoves(plateau);

        Set<Move> expected = new HashSet<>();
        expected.add(new NormalMove(startingPos, new Position(0, 1)));
        expected.add(new NormalMove(startingPos, new Position(1, 0)));
        expected.add(new NormalMove(startingPos, new Position(1, 1)));

        Assertions.assertEquals(expected, moves);
    }
}