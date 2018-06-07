package gui.jeu.board;


import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableNumberValue;
import javafx.beans.value.ObservableValue;
import modele.plateau.Position;

public class GraveyardPosition implements PositionBoard {
    private final GraveyardController graveyardController;

    public GraveyardPosition(GraveyardController graveyardController) {
        this.graveyardController = graveyardController;
    }

    @Override
    public ObservableValue<Number> getX() {
        return Bindings.add(getRelativeX(), graveyardController.getXOffset());
    }

    private ObservableNumberValue getRelativeX() {
        if (graveyardController.isLeftToRight()) {
            if (graveyardController.getPiecesDansGraveyard().get() < Position.LIMITE) {
                return new SimpleIntegerProperty(0);
            } else {
                return graveyardController.getLargeur();
            }
        } else {
            if (graveyardController.getPiecesDansGraveyard().get() < Position.LIMITE) {
                return graveyardController.getLargeur();
            } else {
                return new SimpleIntegerProperty(0);
            }
        }
    }

    @Override
    public ObservableValue<Number> getY() {
        int piecesDansGraveyard = graveyardController.getPiecesDansGraveyard().get();

        if (piecesDansGraveyard >= Position.LIMITE) piecesDansGraveyard -= Position.LIMITE;

        return Bindings.multiply(graveyardController.getLargeur(), piecesDansGraveyard);
    }

    public ObservableValue<Number> getLargeur() {
        return graveyardController.getLargeur();
    }

    @Override
    public void notifyPlaced() {
        graveyardController.incrementCounter();
    }
}
