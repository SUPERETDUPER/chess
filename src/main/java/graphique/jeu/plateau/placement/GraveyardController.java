package graphique.jeu.plateau.placement;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableNumberValue;
import modele.util.Position;

/**
 * Controlle un des 2 graveyards (endroits où les pièces mangées vont)
 * TODO Bouger au modele pour que les pièces mangés puivent être sauvegardées
 */
public class GraveyardController {
    /**
     * Le nombre de pièces dans le graveyard
     */
    private final ReadOnlyIntegerWrapper piecesDansGraveyard = new ReadOnlyIntegerWrapper(0);

    /**
     * Le décalage sur l'axe des X
     */
    private final ObservableNumberValue xOffset;

    /**
     * Si les 2 colonnes devraient se remplir de gauche à droite ou de droite à gauche
     */
    private final boolean gaucheADroite;

    /**
     * La hauteur des colonnes
     */
    private final ObservableNumberValue height;

    /**
     * La largeur totale du graveyard
     */
    private final NumberBinding largeurTotale;

    /**
     * @param height        la hauteur d'une colonne
     * @param gaucheADroite si les pièces devraient être placés en colonne de gauche à droite ou pas
     */
    public GraveyardController(ObservableNumberValue height, boolean gaucheADroite) {
        this(height, gaucheADroite, new SimpleIntegerProperty(0));
    }

    /**
     * @param height        la hauteur d'une colonne
     * @param gaucheADroite si les pièces devraient être placés en colonne de gauche à droite ou pas
     * @param xOffset       le offset du graveyard par rapport au contenaire
     */
    public GraveyardController(ObservableNumberValue height, boolean gaucheADroite, ObservableNumberValue xOffset) {
        this.height = height;
        this.xOffset = xOffset;
        this.gaucheADroite = gaucheADroite;

        this.largeurTotale = Bindings.multiply(height, getTotalWidthRatio());
    }

    public ObservableNumberValue getLargeurTotale() {
        return largeurTotale;
    }

    /**
     * @return la largeur en fonction de la hauteur
     */
    public double getTotalWidthRatio() {
        return 2.0 / Position.LIMITE;
    }

    /**
     * @return un objet qui représente la position dans la prochaine pièce à mettre dans le graveyard
     */
    public PositionGraphique getNextGraveyardPosition() {
        return new PositionGraphique(height) {
            @Override
            public ObservableNumberValue getX() {
                //Si c'est la colonne de gauche ou la colonne de droite
                if ((gaucheADroite && piecesDansGraveyard.get() < Position.LIMITE) ||
                        (!gaucheADroite && piecesDansGraveyard.get() >= Position.LIMITE)) {
                    return xOffset;
                } else {
                    return Bindings.divide(height, Position.LIMITE).add(xOffset);
                }
            }

            @Override
            public NumberBinding getY() {
                //Le nombre de pièces déjà là
                int piecesDansGraveyard = GraveyardController.this.piecesDansGraveyard.get();

                //Le nombre de pièces dans la colonne
                if (piecesDansGraveyard >= Position.LIMITE) piecesDansGraveyard -= Position.LIMITE;

                //La position
                return Bindings.divide(height, Position.LIMITE).multiply(piecesDansGraveyard);
            }

            @Override
            public void notifyPlaced() {
                //Augmenter le nombre de pièces par 1
                piecesDansGraveyard.set(piecesDansGraveyard.get() + 1);
            }
        };
    }
}
