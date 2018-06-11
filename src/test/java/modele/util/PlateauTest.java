package modele.util;

import modele.pieces.Piece;
import modele.pieces.Roi;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PlateauTest {

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

        Plateau plateau = new Plateau();
        plateau.ajouter(positionInitiale, piece);

        Assertions.assertEquals(positionInitiale, plateau.getPosition(piece)); //1
        Assertions.assertNull(plateau.getPosition(new Roi(Couleur.BLANC))); //2
        Assertions.assertNotNull(plateau.getPiece(new Position(0, 0))); //3
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

        Plateau plateau = new Plateau();
        plateau.ajouter(positionInitiale, piece);

        Assertions.assertEquals(piece, plateau.getPiece(positionInitiale));
        Assertions.assertNull(plateau.getPiece(new Position(0, 1)));
    }

    @Test
    void removePiece() {
        Piece piece = new Roi(Couleur.BLANC);
        Position positionInitiale = new Position(0, 0);

        Plateau plateau = new Plateau();
        plateau.ajouter(positionInitiale, piece);

        plateau.removePiece(positionInitiale);
        Assertions.assertNull(plateau.getPiece(positionInitiale));
        Assertions.assertNull(plateau.getPosition(piece));
    }
}