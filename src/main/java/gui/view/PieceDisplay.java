package gui.view;

import javafx.beans.binding.NumberBinding;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import modele.pieces.Piece;
import modele.plateau.Position;

import java.util.function.Consumer;

public class PieceDisplay extends StackPane {
    private static final float HEIGHT_TO_FONT_RATIO = 0.75F;

    private final Piece piece;
    private final NumberBinding taille;

    public PieceDisplay(Piece piece, NumberBinding taille, Consumer<Piece> onClickListener) {
        super();

        this.piece = piece;
        this.taille = taille;
        this.prefHeightProperty().bind(this.taille);
        this.prefWidthProperty().bind(taille);
        this.setOnMouseClicked(event -> onClickListener.accept(this.piece));

        Text text = new Text(Character.toString((char) piece.getNumeroUnicode()));
        taille.addListener((observable, oldValue, newValue) -> text.setFont(new Font(newValue.doubleValue() * HEIGHT_TO_FONT_RATIO)));

        this.getChildren().add(text);
    }

    public void setPosition(Position position) {
        this.layoutXProperty().bind(taille.multiply(position.getColonne()));
        this.layoutYProperty().bind(taille.multiply(position.getRangee()));
    }

    public Piece getPiece() {
        return piece;
    }
}