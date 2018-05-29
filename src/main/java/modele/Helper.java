package modele;

import modele.pieces.Piece;
import modele.plateau.Plateau;
import org.jetbrains.annotations.NotNull;

/**
 * Classe avec des méthodes utiles
 */
class Helper {
    /**
     * @return vrai si la pièce peut se faire manger par une pièce de l'autre couleur
     */
    static boolean isPieceAttaquer(@NotNull Plateau plateau, Piece piece) {
        for (Piece attaqueur : plateau.iteratePieces()) {
            if (attaqueur.getCouleur() != piece.getCouleur() && attaqueur.attaquePosition(plateau, plateau.getPosition(piece))) {
                return true;
            }
        }

        return false;
    }
}
