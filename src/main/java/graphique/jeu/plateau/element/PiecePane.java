package graphique.jeu.plateau.element;

import graphique.jeu.plateau.placement.PositionGraphique;
import javafx.scene.CacheHint;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import modele.pieces.Piece;

/**
 * Une pièce affichée sur l'écran
 */
public class PiecePane extends StackPane {
    /**
     * La taille du font du text par rapport à la case
     */
    private static final float RAPPORT_TAILLE_FONT_SIZE = 0.75F;

    /**
     * La pièce qui se fait afficher
     */
    private final Piece piece;

    /**
     * Le text qui montre la pièce
     */
    private final Text text = new Text();

    /**
     * @param piece    la pièce à afficher
     * @param position la position de la pièce
     */
    public PiecePane(Piece piece, PositionGraphique position) {
        super();

        this.piece = piece;

        //Attacher la taille de la pièce
        this.prefHeightProperty().bind(position.getTaille());
        this.prefWidthProperty().bind(position.getTaille());

        //Attacher la pièce à sa position
        bind(position);

        //Ajouter le text
        text.setText(Character.toString((char) this.piece.getNumeroUnicode()));
        this.getChildren().add(text);

        //Faire que le text reste propertionelle à la taille de la case
        this.prefWidthProperty().addListener(
                (observable, oldValue, newValue) ->
                        text.setFont(new Font(newValue.doubleValue() * RAPPORT_TAILLE_FONT_SIZE))
        );
    }

    public Piece getPiece() {
        return piece;
    }

    /**
     * Place la pièce à la position
     */
    public void bind(PositionGraphique position) {
        this.layoutXProperty().bind(position.getX());
        this.layoutYProperty().bind(position.getY());
        position.notifyPlaced();
        this.setCacheHint(CacheHint.DEFAULT);
    }

    /**
     * Détacher la pièce
     */
    public void unBind() {
        this.setCacheHint(CacheHint.SPEED);
        this.layoutXProperty().unbind();
        this.layoutYProperty().unbind();
    }

    /**
     * ATTENTION : Si les coordonées X, Y sont identiques mais la fonction binding est différente retourne vraie
     * Ex. Si tout à une hauteur et largeur de 0 retourne vrai
     *
     * @return si la pièce est à la position
     */
    public boolean isAtPosition(PositionGraphique position) {
        return this.layoutXProperty().isBound() &&
                this.layoutYProperty().isBound() &&
                this.getLayoutX() == position.getX().getValue().doubleValue() &&
                this.getLayoutY() == position.getY().getValue().doubleValue();
    }
}