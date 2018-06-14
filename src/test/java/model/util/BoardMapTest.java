package model.util;

import model.pieces.King;
import model.pieces.Piece;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BoardMapTest {

    /**
     * Verify
     * 1. getPosition works
     * 2. getPosition is null if piece non-existent
     * 3. getPosition works with two identical positions
     */
    @Test
    void getPosition() {
        Piece piece = new King(Colour.WHITE);
        Position start = new Position(0, 0);

        BoardMap boardMap = new BoardMap();
        boardMap.add(start, piece);

        Assertions.assertEquals(start, boardMap.getPosition(piece)); //1
        Assertions.assertNull(boardMap.getPosition(new King(Colour.WHITE))); //2
        Assertions.assertNotNull(boardMap.getPiece(new Position(0, 0))); //3
    }

    /**
     * Verify that
     * 1. getPiece works
     * 2. getPiece is null if the position does not have a piece
     */
    @Test
    void getPiece() {
        Piece piece = new King(Colour.WHITE);
        Position start = new Position(0, 0);

        BoardMap boardMap = new BoardMap();
        boardMap.add(start, piece);

        Assertions.assertEquals(piece, boardMap.getPiece(start));
        Assertions.assertNull(boardMap.getPiece(new Position(0, 1)));
    }

    @Test
    void removePiece() {
        Piece piece = new King(Colour.WHITE);
        Position start = new Position(0, 0);

        BoardMap boardMap = new BoardMap();
        boardMap.add(start, piece);

        boardMap.removePiece(start);
        Assertions.assertNull(boardMap.getPiece(start));
        Assertions.assertNull(boardMap.getPosition(piece));
    }
}