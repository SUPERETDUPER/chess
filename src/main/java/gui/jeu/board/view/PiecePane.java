package gui.jeu.board.view;

import gui.jeu.board.PositionBoard;
import javafx.beans.binding.NumberBinding;
import javafx.beans.value.ObservableValue;
import javafx.scene.CacheHint;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import modele.pieces.Piece;

/**
 * Une pièce affichée sur l'écran
 */
public class PiecePane extends StackPane {
    private static final float RAPPORT_TAILLE_FONT_SIZE = 0.75F;

    /**
     * La pièce qui se fait afficher
     */
    private final Piece piece;

    /**
     * @param taille la taille de la case
     * @param piece             la pièce à afficher
     */
    public PiecePane(Piece piece, PositionBoard position, NumberBinding taille) {
        super();

        this.piece = piece;

        //Attacher la displayCalculator
        this.prefHeightProperty().bind(taille);
        this.prefWidthProperty().bind(taille);
        bind(position);

        //Ajouter le text
        Text text = new Text(Character.toString((char) piece.getNumeroUnicode()));
        this.getChildren().add(text);

        //Faire que la displayCalculator du text reste propertionelle
        taille.addListener(
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
    public void bind(PositionBoard position) {
        bind(position.getX(), position.getY());
    }

    private void bind(ObservableValue<? extends Number> x, ObservableValue<? extends Number> y) {
        System.out.println("Bind");
        this.layoutXProperty().bind(x);
        this.layoutYProperty().bind(y);
        this.setCacheHint(CacheHint.DEFAULT);
    }

    public void unBind() {
        System.out.println("Unbind");
        this.setCacheHint(CacheHint.SPEED);
        this.layoutXProperty().unbind();
        this.layoutYProperty().unbind();
    }

    public boolean isAtPosition(PositionBoard position) {
        return this.layoutXProperty().isBound() &&
                this.layoutYProperty().isBound() &&
                this.getLayoutX() == position.getX().getValue().doubleValue() &&
                this.getLayoutY() == position.getY().getValue().doubleValue();
    }
}