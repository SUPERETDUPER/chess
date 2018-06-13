package ui.game.components;

import javafx.beans.value.ObservableNumberValue;
import javafx.scene.CacheHint;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.pieces.Piece;
import ui.game.layout.GraphicPosition;

/**
 * A pane that displays a pièce
 */
public class PiecePane extends StackPane {
    /**
     * The ratio for the size of the pane to the font size
     */
    private static final float SIZE_TO_FONT_RATIO = 0.75F;

    /**
     * The piece in the pane
     */
    private final Piece piece;

    /**
     * The text showing the piece
     */
    private final Text text = new Text();

    /**
     * The pane's current position
     */
    private GraphicPosition currentPosition;

    /**
     * @param piece    the piece to show
     * @param position the position of the piece
     * @param size the size of the pane (width/height)
     */
    public PiecePane(Piece piece, GraphicPosition position, ObservableNumberValue size) {
        super();

        this.piece = piece;
        this.currentPosition = position;

        //Bind the pane's width/height
        this.prefHeightProperty().bind(size);
        this.prefWidthProperty().bind(size);

        //Bind the pane to it's position
        bind(position);

        //Add the text to the pane
        setText();
        this.getChildren().add(text);

        //Bind the text's font to the pane's size
        this.prefWidthProperty().addListener(
                (observable, oldValue, newValue) ->
                        text.setFont(new Font(newValue.doubleValue() * SIZE_TO_FONT_RATIO))
        );
    }

    /**
     * Update the text based on the piece (actually matters for pawn promotion where pawn becomes queen)
     */
    public void setText() {
        text.setText(Character.toString((char) piece.getUnicode())); //Set the text to the unicode value of the piece
    }

    public Piece getPiece() {
        return piece;
    }

    /**
     * Binds the piece to its position
     */
    public synchronized void bind(GraphicPosition position) {
        this.currentPosition = position;

        this.layoutXProperty().bind(position.getX());
        this.layoutYProperty().bind(position.getY());

        this.setCacheHint(CacheHint.DEFAULT);
    }

    /**
     * Unbinds the piece from it's position
     */
    public synchronized void unBind() {
        this.currentPosition = null;
        this.setCacheHint(CacheHint.SPEED);
        this.layoutXProperty().unbind();
        this.layoutYProperty().unbind();
    }

    /**
     * @return true if the piece is at the position
     */
    public synchronized boolean isAtPosition(GraphicPosition position) {
        return position.equals(currentPosition);
    }

    public GraphicPosition getCurrentPosition() {
        return currentPosition;
    }
}