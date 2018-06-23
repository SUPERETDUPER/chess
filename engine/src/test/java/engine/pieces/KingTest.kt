package engine.pieces

import engine.GameData
import engine.moves.BaseMove
import engine.moves.Move
import engine.util.PieceMap
import engine.util.Colour
import engine.util.Position
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*

internal class KingTest {

    @Test
    fun generateMoves() {
        val king = King(Colour.WHITE)

        val boardMap = PieceMap()
        val startingPos = Position(0, 0)
        boardMap.put(startingPos, king)

        val moves = king.generatePossibleMoves(GameData(boardMap), startingPos)

        val expected = LinkedList<Move>()
        expected.add(BaseMove(startingPos, Position(0, 1)))
        expected.add(BaseMove(startingPos, Position(1, 0)))
        expected.add(BaseMove(startingPos, Position(1, 1)))

        Assertions.assertEquals(expected, moves)
    }
}