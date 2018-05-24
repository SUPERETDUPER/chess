package modele.pieces;

import modele.board.Board;
import modele.board.Position;
import modele.moves.Move;
import modele.moves.NormalMove;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

class RoiTest {

    @Test
    void generateMoves() {
        Roi roi = new Roi(true);

        Board board = new Board();
        Position startingPos = new Position(0, 0);
        board.ajouter(startingPos, roi);

        Set<Move> moves = roi.generateAllMoves(board);

        Set<Move> expected = new HashSet<>();
        expected.add(new NormalMove(startingPos, new Position(0, 1)));
        expected.add(new NormalMove(startingPos, new Position(1, 0)));
        expected.add(new NormalMove(startingPos, new Position(1, 1)));

        Assertions.assertEquals(expected, moves);
    }
}