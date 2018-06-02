package gui.jeu.board.view;

import javafx.beans.binding.NumberBinding;
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
    private final NumberBinding taille;

    private Position position;


    /**
     * @param piece  la pièce à afficher
     * @param taille la taille de la boite
     */
    public PiecePane(Piece piece, NumberBinding taille, Position position) {
        super();

        this.piece = piece;
        this.taille = taille;

        //Attacher la taille
        this.prefHeightProperty().bind(taille);
        this.prefWidthProperty().bind(taille);
        bind(position);

        //Ajouter le text
        Text text = new Text(Character.toString((char) piece.getNumeroUnicode()));
        this.getChildren().add(text);

        //Faire que la taille du text reste propertionelle
        this.taille.addListener(
                (observable, oldValue, newValue) ->
                        text.setFont(new Font(newValue.doubleValue() * RAPPORT_TAILLE_FONT_SIZE))
        );
    }

    public Piece getPiece() {
        return piece;
    }

    public Position getPosition() {
        return position;
    }

    /**
     * Place la pièce à la position
     */
    public void bind(Position position) {
        this.position = position;
        this.layoutXProperty().bind(taille.multiply(position.getColonne()));
        this.layoutYProperty().bind(taille.multiply(position.getRangee()));
        this.setCacheHint(CacheHint.DEFAULT);
    }

    public void unBind() {
        this.setCacheHint(CacheHint.SPEED);
        this.layoutXProperty().unbind();
        this.layoutYProperty().unbind();
    }

    public boolean isAtPosition(Position position) {
        return this.layoutXProperty().isBound() && this.layoutYProperty().isBound() && this.position.equals(position);
    }
}