package graphique.jeu.plateau.placement;

import graphique.jeu.plateau.element.PiecePane;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableNumberValue;
import modele.pieces.Piece;
import modele.util.Position;

import java.util.Stack;

/**
 * Controlle un des 2 graveyards (endroits où les pièces mangées vont)
 */
public class GraveyardController {
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

    private final Stack<Piece> piecesInGraveyard = new Stack<>();

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
        return new Emplacement();
    }

    public boolean isInGraveyard(Piece piece) {
        return piecesInGraveyard.contains(piece);
    }

    private class Emplacement extends PositionGraphique {
        private final int position = piecesInGraveyard.size();

        Emplacement() {
            super(GraveyardController.this.height);
        }

        @Override
        public ObservableNumberValue getX() {
            //Si c'est la colonne de gauche ou la colonne de droite
            if ((gaucheADroite && position < modele.util.Position.LIMITE) ||
                    (!gaucheADroite && position >= modele.util.Position.LIMITE)) {
                return xOffset;
            } else {
                return Bindings.divide(height, modele.util.Position.LIMITE).add(xOffset);
            }
        }

        @Override
        public NumberBinding getY() {
            return Bindings
                    .divide(height, modele.util.Position.LIMITE)
                    .multiply(position < modele.util.Position.LIMITE ? position : position - modele.util.Position.LIMITE);
        }

        @Override
        public void notifyPlaced(PiecePane piecePane) {
            piecesInGraveyard.push(piecePane.getPiece());
        }

        @Override
        public void notifyRemoved(PiecePane piecePane) {
            Piece piecePoped = piecesInGraveyard.pop();
            if (piecePoped != piecePane.getPiece()) throw new RuntimeException("Piece removed not last in stack");
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) return false;
            if (!(obj instanceof Emplacement)) return false;
            return ((Emplacement) obj).position == this.position;
        }

        @Override
        public String toString() {
            return "Graveyard pos: " + position;
        }
    }
}
