package gui.jeu.board;


import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableNumberValue;
import javafx.beans.value.ObservableValue;
import modele.pieces.Couleur;
import modele.plateau.Position;

public class GraveyardPosition implements PositionBoard {
    private final Couleur couleur;
    private final ObservableNumberValue hauteur;
    private final GraveyardController graveyardController;

    public GraveyardPosition(Couleur couleur, ObservableNumberValue hauteur, GraveyardController graveyardController) {
        this.couleur = couleur;
        this.hauteur = hauteur;
        this.graveyardController = graveyardController;
    }

    @Override
    public ObservableValue<Number> getX() {
        if (couleur == Couleur.BLANC) {
            return new SimpleIntegerProperty(0);
        } else {
            return Bindings.add(hauteur, graveyardController.getLargeur()).add(graveyardController.getSpacing());
        }
    }

    @Override
    public ObservableValue<Number> getY() {
        if (couleur == Couleur.BLANC) {
            return Bindings.divide(hauteur, graveyardController.getPiecesDansGraveyardBlanc())
                    .multiply(graveyardController.getPiecesDansGraveyardBlanc().getValue() - 1);
        } else {
            return Bindings.divide(hauteur, graveyardController.getPiecesDansGraveyardNoir())
                    .multiply(graveyardController.getPiecesDansGraveyardNoir().getValue() - 1);
        }
    }

    public ObservableValue<Number> getLargeur() {
        return Bindings.divide(hauteur, Position.LIMITE);
    }
}
