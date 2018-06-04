package gui.jeu.board;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableNumberValue;
import javafx.beans.value.ObservableValue;
import modele.pieces.Couleur;
import modele.plateau.Position;

public class DisplayCalculator {
    private final static double GRAVEYARD_SPACING_RATIO = 0.1;
    private static final double WIDTH_HEIGHT_RATIO = (Position.LIMITE + 2 + 2 * GRAVEYARD_SPACING_RATIO) / Position.LIMITE;

    private final NumberBinding taille;
    private final ObservableNumberValue hauteur;
    private final NumberBinding spacingWidth;

    private final SimpleIntegerProperty piecesDansGraveyardBlanc = new SimpleIntegerProperty(0);
    private final SimpleIntegerProperty piecesDansGraveyardNoir = new SimpleIntegerProperty(0);

    public DisplayCalculator(ObservableNumberValue hauteur) {
        this.hauteur = hauteur;
        taille = Bindings.divide(hauteur, Position.LIMITE);
        spacingWidth = taille.multiply(GRAVEYARD_SPACING_RATIO);
    }

    public ObservableValue<Number> getX(Position position) {
        return taille.multiply(position.getColonne()).add(spacingWidth).add(taille);
    }

    public ObservableValue<Number> getY(Position position) {
        return taille.multiply(position.getRangee());
    }

    ObservableValue<Number> getGraveyardY(Couleur couleur) {
        if (couleur == Couleur.BLANC) {
            return Bindings.divide(hauteur, piecesDansGraveyardBlanc).multiply(piecesDansGraveyardBlanc.getValue() - 1);
        } else {
            return Bindings.divide(hauteur, piecesDansGraveyardNoir).multiply(piecesDansGraveyardNoir.getValue() - 1);
        }
    }

    public NumberBinding getTaille() {
        return taille;
    }

    void incrementGraveyardNoir() {
        piecesDansGraveyardNoir.set(piecesDansGraveyardNoir.get() + 1);
    }

    void incrementGraveyardBlanc() {
        piecesDansGraveyardBlanc.set(piecesDansGraveyardBlanc.get() + 1);
    }

    public double getWidthHeightRatio() {
        return WIDTH_HEIGHT_RATIO;
    }

    public ObservableNumberValue getGraveyardWidth() {
        return spacingWidth.add(taille);
    }
}
