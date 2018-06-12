package model.util;

import model.pieces.King;
import model.pieces.Piece;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BoardMapTest {

    /**
     * Vérifie que
     * 1. getPosition fonctionne
     * 2. getPosition et null si la pièce n'existe pas
     * 3. getPosition fonctionne avec deux fonctions identiques
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
     * Vérifie que
     * 1. getPiece fonctionne
     * 2. getPiece et null si la position n'existe pas
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