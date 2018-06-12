package gui.gamewindow.boardregion.components;

import gui.gamewindow.boardregion.layout.Layout;
import javafx.scene.CacheHint;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.pieces.Piece;

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

    private Layout currentPosition;

    /**
     * @param piece    la pièce à afficher
     * @param position la position de la pièce
     */
    public PiecePane(Piece piece, Layout position) {
        super();

        this.piece = piece;
        this.currentPosition = position;

        //Attacher la taille de la pièce
        this.prefHeightProperty().bind(position.getTaille());
        this.prefWidthProperty().bind(position.getTaille());

        //Attacher la pièce à sa position
        bind(position);

        //Ajouter le text
        setText();
        this.getChildren().add(text);

        //Faire que le text reste propertionelle à la taille de la case
        this.prefWidthProperty().addListener(
                (observable, oldValue, newValue) ->
                        text.setFont(new Font(newValue.doubleValue() * RAPPORT_TAILLE_FONT_SIZE))
        );
    }

    /**
     * Met à jour le text
     */
    public void setText() {
        text.setText(Character.toString((char) piece.getNumeroUnicode()));
    }

    public Piece getPiece() {
        return piece;
    }

    /**
     * Place la pièce à la position
     */
    public synchronized void bind(Layout position) {
        this.currentPosition = position;
        this.layoutXProperty().bind(position.getX());
        this.layoutYProperty().bind(position.getY());
        position.notifyPlaced(this);
        this.setCacheHint(CacheHint.DEFAULT);
    }

    /**
     * Détacher la pièce
     */
    public synchronized void unBind() {
        currentPosition.notifyRemoved(this);
        this.currentPosition = null;
        this.setCacheHint(CacheHint.SPEED);
        this.layoutXProperty().unbind();
        this.layoutYProperty().unbind();
    }

    /**
     * @return si la pièce est à la position
     */
    public synchronized boolean isAtPosition(Layout position) {
        return position.equals(currentPosition);
    }

    public Layout getCurrentPosition() {
        return currentPosition;
    }
}