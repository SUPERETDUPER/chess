package gui.view;

import javafx.beans.binding.NumberBinding;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Une pièce afficher sur l'écran
 */
public class Piece extends StackPane {
    private static final float RAPPORT_TAILLE_FONT_SIZE = 0.75F;

    /**
     * La pièce qui se fait afficher
     */
    private final modele.pieces.Piece piece;

    /**
     * @param piece  la pièce à afficher
     * @param taille la taille de la boite
     */
    public Piece(modele.pieces.Piece piece, NumberBinding taille) {
        super();

        this.piece = piece;

        //Attacher la taille
        this.prefHeightProperty().bind(taille);
        this.prefWidthProperty().bind(taille);

        //Ajouter le text
        Text text = new Text(Character.toString((char) piece.getNumeroUnicode()));
        this.getChildren().add(text);

        //Faire que la taille du text reste propertionelle
        taille.addListener(
                (observable, oldValue, newValue) ->
                        text.setFont(new Font(newValue.doubleValue() * RAPPORT_TAILLE_FONT_SIZE))
        );
    }

    public modele.pieces.Piece getPiece() {
        return piece;
    }
}