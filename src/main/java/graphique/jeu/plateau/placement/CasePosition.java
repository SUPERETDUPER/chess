package graphique.jeu.plateau.placement;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.value.ObservableNumberValue;
import modele.util.Position;

/**
 * La position d'une case sur le util
 */
public class CasePosition extends PositionGraphique {
    private final Position position;
    private final ObservableNumberValue xOffset;

    public CasePosition(Position position, ObservableNumberValue hauteur, ObservableNumberValue xOffset) {
        super(hauteur);
        this.position = position;
        this.xOffset = xOffset;
    }

    @Override
    public NumberBinding getX() {
        return Bindings.divide(hauteurDuPlateau, Position.LIMITE).multiply(position.getColonne()).add(xOffset);
    }

    @Override
    public NumberBinding getY() {
        return Bindings.divide(hauteurDuPlateau, Position.LIMITE).multiply(position.getRangee());
    }

    public Position getPosition() {
        return position;
    }

    @Override
    public void notifyPlaced() {
    }
}
