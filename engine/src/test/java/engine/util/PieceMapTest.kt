package engine.util

import engine.pieces.King
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class PieceMapTest {

    /**
     * Verify
     * 1. get works
     * 2. get is null if pieceMap non-existent
     * 3. get works with two identical positions
     */
    @Test
    fun getPosition() {
        val piece = King(Colour.WHITE)
        val start = Position(0, 0)

        val boardMap = PieceMap()
        boardMap.put(start, piece)

        Assertions.assertEquals(start, boardMap[piece]) //1
        Assertions.assertNull(boardMap[King(Colour.WHITE)]) //2
        Assertions.assertNotNull(boardMap[Position(0, 0)]) //3
    }

    /**
     * Verify that
     * 1. get works
     * 2. get is null if the position does not have a pieceMap
     */
    @Test
    fun getPiece() {
        val piece = King(Colour.WHITE)
        val start = Position(0, 0)

        val boardMap = PieceMap()
        boardMap.put(start, piece)

        Assertions.assertEquals(piece, boardMap[start])
        Assertions.assertNull(boardMap[Position(0, 1)])
    }

    @Test
    fun removePiece() {
        val piece = King(Colour.WHITE)
        val start = Position(0, 0)

        val boardMap = PieceMap()
        boardMap.put(start, piece)

        boardMap.removePiece(start)
        Assertions.assertNull(boardMap[start])
        Assertions.assertNull(boardMap[piece])
    }
}