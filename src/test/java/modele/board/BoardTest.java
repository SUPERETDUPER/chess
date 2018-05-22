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
        board.put(piece, positionInitiale);
        board.movePiece(positionInitiale, positionFinale);

        Assertions.assertEquals(board.getPiece(positionFinale), piece);
        Assertions.assertEquals(positionFinale, board.getPosition(piece));

        //2.
        Assertions.assertThrows(IllegalArgumentException.class, () -> board.movePiece(positionInitiale, positionFinale));
    }
}