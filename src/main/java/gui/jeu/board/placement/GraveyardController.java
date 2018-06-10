package gui.jeu.board.placement;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableNumberValue;
import modele.plateau.PositionBase;

/**
 * Controlle un des 2 graveyards (endroits où les pièces mangées vont)
 */
public class GraveyardController {
    private final ReadOnlyIntegerWrapper piecesDansGraveyard = new ReadOnlyIntegerWrapper(0);
    private final ObservableNumberValue xOffset;
    private final boolean leftToRight;
    private final ObservableNumberValue height;

    private final NumberBinding largeurTotale;

    /**
     * @param height      la hauteur d'une colonne
     * @param leftToRight si les pièces devraient être placés en colonne de gauche à droite ou pas
     */
    public GraveyardController(ObservableNumberValue height, boolean leftToRight) {
        this(height, leftToRight, new SimpleIntegerProperty(0));
    }

    /**
     * @param height      la hauteur d'une colonne
     * @param leftToRight si les pièces devraient être placés en colonne de gauche à droite ou pas
     * @param xOffset     le offset du graveyard par rapport au contenaire
     */
    public GraveyardController(ObservableNumberValue height, boolean leftToRight, ObservableNumberValue xOffset) {
        this.height = height;
        this.xOffset = xOffset;
        this.leftToRight = leftToRight;

        this.largeurTotale = Bindings.multiply(height, getTotalWidthRatio());
    }

    public ObservableNumberValue getLargeurTotale() {
        return largeurTotale;
    }

    public double getTotalWidthRatio() {
        return 2.0 / PositionBase.LIMITE;
    }

    /**
     * @return la position pour la prochaine pièce à mettre dans le graveyard
     */
    public PositionGraphique getNextGraveyardPosition() {
        return new PositionGraphique(height) {
            @Override
            public NumberBinding getX() {
                ObservableNumberValue xRelatif;
                if ((leftToRight && piecesDansGraveyard.get() < PositionBase.LIMITE) ||
                        (!leftToRight && piecesDansGraveyard.get() >= PositionBase.LIMITE)) {
                    xRelatif = new SimpleIntegerProperty(0);
                } else {
                    xRelatif = Bindings.divide(height, PositionBase.LIMITE);
                }

                return Bindings.add(xRelatif, xOffset);
            }

            @Override
            public NumberBinding getY() {
                //Le nombre de pièces déjà là
                int piecesDansGraveyard = GraveyardController.this.piecesDansGraveyard.get();

                //Le nombre de pièces dans la colonne
                if (piecesDansGraveyard >= PositionBase.LIMITE) piecesDansGraveyard -= PositionBase.LIMITE;

                //La position
                return Bindings.divide(height, PositionBase.LIMITE).multiply(piecesDansGraveyard);
            }

            @Override
            public void notifyPlaced() {
                //Augmenter le nombre de pièces par 1
                piecesDansGraveyard.set(piecesDansGraveyard.get() + 1);
            }
        };
    }
}
