package gui.jeu.board.placement;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.value.ObservableNumberValue;
import modele.plateau.Offset;
import modele.plateau.Position;
import modele.plateau.PositionBase;

/**
 * La position d'une case sur le plateau
 */
public class CasePosition extends PositionGraphique implements Position {
    private final Position position;
    private final ObservableNumberValue xOffset;

    public CasePosition(Position position, ObservableNumberValue hauteur, ObservableNumberValue xOffset) {
        super(hauteur);
        this.position = position;
        this.xOffset = xOffset;
    }

    @Override
    public NumberBinding getX() {
        return Bindings.divide(hauteurDuPlateau, PositionBase.LIMITE).multiply(position.getColonne()).add(xOffset);
    }

    @Override
    public NumberBinding getY() {
        return Bindings.divide(hauteurDuPlateau, PositionBase.LIMITE).multiply(position.getRangee());
    }

    @Override
    public int getRangee() {
        return position.getRangee();
    }

    @Override
    public int getColonne() {
        return position.getColonne();
    }

    @Override
    public boolean isValid() {
        return position.isValid();
    }

    @Override
    public Position decaler(Offset offset) {
        return position.decaler(offset);
    }

    @Override
    public void notifyPlaced() {
    }
}
