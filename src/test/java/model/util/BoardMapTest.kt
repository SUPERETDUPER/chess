package model.util

import model.pieces.King
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class BoardMapTest {

    /**
     * Verify
     * 1. getPosition works
     * 2. getPosition is null if piece non-existent
     * 3. getPosition works with two identical positions
     */
    @Test
    fun getPosition() {
        val piece = King(Colour.WHITE)
        val start = Position(0, 0)

        val boardMap = BoardMap()
        boardMap.add(start, piece)

        Assertions.assertEquals(start, boardMap.getPosition(piece)) //1
        Assertions.assertNull(boardMap.getPosition(King(Colour.WHITE))) //2
        Assertions.assertNotNull(boardMap.getPiece(Position(0, 0))) //3
    }

    /**
     * Verify that
     * 1. getPiece works
     * 2. getPiece is null if the position does not have a piece
     */
    @Test
    fun getPiece() {
        val piece = King(Colour.WHITE)
        val start = Position(0, 0)

        val boardMap = BoardMap()
        boardMap.add(start, piece)

        Assertions.assertEquals(piece, boardMap.getPiece(start))
        Assertions.assertNull(boardMap.getPiece(Position(0, 1)))
    }

    @Test
    fun removePiece() {
        val piece = King(Colour.WHITE)
        val start = Position(0, 0)

        val boardMap = BoardMap()
        boardMap.add(start, piece)

        boardMap.removePiece(start)
        Assertions.assertNull(boardMap.getPiece(start))
        Assertions.assertNull(boardMap.getPosition(piece))
    }
}