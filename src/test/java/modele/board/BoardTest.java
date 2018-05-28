package modele.board;

import modele.pieces.Couleur;
import modele.pieces.Piece;
import modele.pieces.Roi;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BoardTest {

    /**
     * Vérifie que
     * 1. getPosition fonctionne
     * 2. getPosition et null si la pièce n'existe pas
     * 3. getPosition fonctionne avec deux fonctions identiques
     */
    @Test
    void getPosition() {
        Piece piece = new Roi(Couleur.BLANC);
        Position positionInitiale = new Position(0, 0);

        Board board = new Board();
        board.ajouter(positionInitiale, piece);

        Assertions.assertEquals(positionInitiale, board.getPosition(piece)); //1
        Assertions.assertNull(board.getPosition(new Roi(Couleur.BLANC))); //2
        Assertions.assertNotNull(board.getPiece(new Position(0, 0))); //3
    }

    /**
     * Vérifie que
     * 1. getPiece fonctionne
     * 2. getPiece et null si la position n'existe pas
     */
    @Test
    void getPiece() {
        Piece piece = new Roi(Couleur.BLANC);
        Position positionInitiale = new Position(0, 0);

        Board board = new Board();
        board.ajouter(positionInitiale, piece);

        Assertions.assertEquals(piece, board.getPiece(positionInitiale));
        Assertions.assertNull(board.getPiece(new Position(0, 1)));
    }

    @Test
    void removePiece() {
        Piece piece = new Roi(Couleur.BLANC);
        Position positionInitiale = new Position(0, 0);

        Board board = new Board();
        board.ajouter(positionInitiale, piece);

        board.removePiece(positionInitiale);
        Assertions.assertNull(board.getPiece(positionInitiale));
        Assertions.assertNull(board.getPosition(piece));
    }
}