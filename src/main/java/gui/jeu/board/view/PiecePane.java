package gui.jeu.board.view;

import gui.jeu.board.DisplayCalculator;
import javafx.beans.value.ObservableValue;
import javafx.scene.CacheHint;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import modele.pieces.Piece;
import modele.plateau.Position;

/**
 * Une pièce affichée sur l'écran
 */
public class PiecePane extends StackPane {
    private static final float RAPPORT_TAILLE_FONT_SIZE = 0.75F;

    /**
     * La pièce qui se fait afficher
     */
    private final Piece piece;
    private final DisplayCalculator displayCalculator;

    /**
     * @param piece             la pièce à afficher
     * @param displayCalculator la displayCalculator de la boite
     */
    public PiecePane(Piece piece, DisplayCalculator displayCalculator, Position position) {
        super();

        this.piece = piece;
        this.displayCalculator = displayCalculator;

        //Attacher la displayCalculator
        this.prefHeightProperty().bind(displayCalculator.getTaille());
        this.prefWidthProperty().bind(displayCalculator.getTaille());
        bind(position);

        //Ajouter le text
        Text text = new Text(Character.toString((char) piece.getNumeroUnicode()));
        this.getChildren().add(text);

        //Faire que la displayCalculator du text reste propertionelle
        this.displayCalculator.getTaille().addListener(
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
    public void bind(Position position) {
        bind(displayCalculator.getX(position), displayCalculator.getY(position));
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

    public boolean isAtPosition(Position position) {
        return this.layoutXProperty().isBound() &&
                this.layoutYProperty().isBound() &&
                this.getLayoutX() == displayCalculator.getX(position).doubleValue() &&
                this.getLayoutY() == displayCalculator.getY(position).doubleValue();
    }
}