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
            if (graveyardController.getPiecesDansGraveyardBlanc().get() > Position.LIMITE) {
                return new SimpleIntegerProperty(0);
            } else {
                return graveyardController.getLargeur();
            }
        } else {
            if (graveyardController.getPiecesDansGraveyardNoir().get() <= Position.LIMITE) {
                return Bindings.add(hauteur, graveyardController.getLargeurTotal()).add(graveyardController.getSpacing());
            } else {
                return Bindings.add(hauteur, graveyardController.getLargeurTotal()).add(graveyardController.getSpacing())
                        .add(graveyardController.getLargeur());
            }
        }
    }

    @Override
    public ObservableValue<Number> getY() {
        int piecesDansGraveyard = (couleur == Couleur.BLANC ? graveyardController.getPiecesDansGraveyardBlanc() : graveyardController.getPiecesDansGraveyardNoir()).get();

        if (piecesDansGraveyard > Position.LIMITE) piecesDansGraveyard -= Position.LIMITE;

        return Bindings.divide(hauteur, Position.LIMITE).multiply(piecesDansGraveyard - 1);
    }

    public ObservableValue<Number> getLargeur() {
        return Bindings.divide(hauteur, Position.LIMITE);
    }
}
