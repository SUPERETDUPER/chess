package gui.jeu.board;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.value.ObservableNumberValue;
import modele.plateau.Position;

class GraveyardController {
    private final static double GRAVEYARD_SPACING_RATIO = 0.1;

    private final ReadOnlyIntegerWrapper piecesDansGraveyardBlanc = new ReadOnlyIntegerWrapper(0);
    private final ReadOnlyIntegerWrapper piecesDansGraveyardNoir = new ReadOnlyIntegerWrapper(0);
    private final ObservableNumberValue largeur;

    GraveyardController(ObservableNumberValue height) {
        this.largeur = Bindings.divide(height, Position.LIMITE);
    }

    void incrementGraveyardNoir() {
        piecesDansGraveyardNoir.set(piecesDansGraveyardNoir.get() + 1);
    }

    void incrementGraveyardBlanc() {
        piecesDansGraveyardBlanc.set(piecesDansGraveyardBlanc.get() + 1);
    }

    ReadOnlyIntegerProperty getPiecesDansGraveyardBlanc() {
        return piecesDansGraveyardBlanc.getReadOnlyProperty();
    }

    ReadOnlyIntegerProperty getPiecesDansGraveyardNoir() {
        return piecesDansGraveyardNoir.getReadOnlyProperty();
    }

    ObservableNumberValue getLargeur() {
        return Bindings.multiply(largeur, 1 + GRAVEYARD_SPACING_RATIO);
    }

    ObservableNumberValue getSpacing() {
        return Bindings.multiply(largeur, GRAVEYARD_SPACING_RATIO);
    }

    double getTotalWidthRatio() {
        return 2 * (1 + GRAVEYARD_SPACING_RATIO) / Position.LIMITE;
    }
}
