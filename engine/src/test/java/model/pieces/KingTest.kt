package model.pieces

import model.GameData
import model.moves.BaseMove
import model.moves.Move
import model.util.BoardMap
import model.util.Colour
import model.util.Position
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*

internal class KingTest {

    @Test
    fun generateMoves() {
        val king = King(Colour.WHITE)

        val boardMap = BoardMap()
        val startingPos = Position(0, 0)
        boardMap.add(startingPos, king)

        val moves = king.generatePossibleMoves(GameData(boardMap), startingPos)

        val expected = LinkedList<Move>()
        expected.add(BaseMove(startingPos, Position(0, 1)))
        expected.add(BaseMove(startingPos, Position(1, 0)))
        expected.add(BaseMove(startingPos, Position(1, 1)))

        Assertions.assertEquals(expected, moves)
    }
}