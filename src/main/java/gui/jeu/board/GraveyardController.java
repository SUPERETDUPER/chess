package gui.jeu.board;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.value.ObservableNumberValue;
import modele.plateau.Position;

class GraveyardController {
    private final static double GRAVEYARD_SPACING_RATIO = 0.1;

    private final ReadOnlyIntegerWrapper piecesDansGraveyard = new ReadOnlyIntegerWrapper(0);
    private final ObservableNumberValue largeur;
    private final ObservableNumberValue xOffset;
    private final boolean leftToRight;

    GraveyardController(ObservableNumberValue height, ObservableNumberValue xOffset, boolean leftToRight) {
        this.largeur = Bindings.divide(height, Position.LIMITE);
        this.xOffset = xOffset;
        this.leftToRight = leftToRight;
    }

    void incrementCounter() {
        piecesDansGraveyard.set(piecesDansGraveyard.get() + 1);
    }

    ReadOnlyIntegerProperty getPiecesDansGraveyard() {
        return piecesDansGraveyard.getReadOnlyProperty();
    }

    ObservableNumberValue getLargeurTotal() {
        return Bindings.multiply(largeur, 2 + GRAVEYARD_SPACING_RATIO);
    }

    public ObservableNumberValue getLargeur() {
        return largeur;
    }

    ObservableNumberValue getSpacing() {
        return Bindings.multiply(largeur, GRAVEYARD_SPACING_RATIO);
    }

    double getTotalWidthRatio() {
        return (2 + GRAVEYARD_SPACING_RATIO) / Position.LIMITE;
    }

    public ObservableNumberValue getXOffset() {
        return xOffset;
    }

    public boolean isLeftToRight() {
        return leftToRight;
    }
}
