package modele.board;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BoardTest {

    /**
     * Vérifie que
     * 1. Une pièce bouge correctement
     * 2. On ne peut pas bouger une pièce qui n'existe pas
     */
    @Test
    void movePiece() {
        //1.
        Piece piece = new Piece(true) {
        };
        Position positionInitiale = new Position(0, 0);
        Position positionFinale = new Position(6, 6);

        Board board = new Board();
        board.ajouter(positionInitiale, piece);
        board.movePiece(positionInitiale, positionFinale);

        Assertions.assertEquals(piece, board.getPiece(positionFinale));
        Assertions.assertEquals(positionFinale, board.getPosition(piece));

        //2.
        Assertions.assertThrows(IllegalArgumentException.class, () -> board.movePiece(positionInitiale, positionFinale));
    }

    /**
     * Vérifie que
     * 1. getPosition fonctionne
     * 2. getPosition et null si la pièce n'existe pas
     */
    @Test
    void getPosition() {
        Piece piece = new Piece(true) {
        };
        Position positionInitiale = new Position(0, 0);

        Board board = new Board();
        board.ajouter(positionInitiale, piece);

        Assertions.assertEquals(positionInitiale, board.getPosition(piece));
        Assertions.assertNull(board.getPosition(new Piece(true) {
        }));
    }

    /**
     * Vérifie que
     * 1. getPiece fonctionne
     * 2. getPiece et null si la position n'existe pas
     */
    @Test
    void getPiece() {
        Piece piece = new Piece(true) {
        };
        Position positionInitiale = new Position(0, 0);

        Board board = new Board();
        board.ajouter(positionInitiale, piece);

        Assertions.assertEquals(piece, board.getPiece(positionInitiale));
        Assertions.assertNull(board.getPiece(new Position(0, 0)));
    }

    @Test
    void removePiece() {
        Piece piece = new Piece(true) {
        };
        Position positionInitiale = new Position(0, 0);

        Board board = new Board();
        board.ajouter(positionInitiale, piece);

        board.removePiece(positionInitiale);
        Assertions.assertNull(board.getPiece(positionInitiale));
        Assertions.assertNull(board.getPosition(piece));
    }
}