package model.pieces;

import model.moves.BaseMove;
import model.moves.Move;
import model.util.BoardMap;
import model.util.Colour;
import model.util.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.LinkedList;

class KingTest {

    @Test
    void generateMoves() {
        King king = new King(Colour.WHITE);

        BoardMap boardMap = new BoardMap();
        Position startingPos = new Position(0, 0);
        boardMap.add(startingPos, king);

        Collection<Move> moves = king.generateAllMoves(boardMap, startingPos);

        Collection<Move> expected = new LinkedList<>();
        expected.add(new BaseMove(startingPos, new Position(0, 1)));
        expected.add(new BaseMove(startingPos, new Position(1, 0)));
        expected.add(new BaseMove(startingPos, new Position(1, 1)));

        Assertions.assertEquals(expected, moves);
    }
}