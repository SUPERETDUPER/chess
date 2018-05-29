package modele;

import modele.pieces.Piece;
import modele.pieces.Roi;
import modele.plateau.Plateau;
import org.jetbrains.annotations.NotNull;

public class Helper {
    public static boolean roiInCheck(@NotNull Plateau plateau, Roi roi) {
        for (Piece piece : plateau.iteratePieces()) {
            if (piece.getCouleur() != roi.getCouleur() && piece.attacksPosition(plateau, plateau.getPosition(roi))) {
                return true;
            }
        }

        return false;
    }
}
