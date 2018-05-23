package gui;

import javafx.fxml.FXML;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import modele.board.Position;
import modele.pieces.Piece;

/**
 * Controle une case
 */
public class CaseController {
    private static final float FONT_TO_HEIGHT_RATIO = 0.75F;
    @FXML
    private StackPane root;
    @FXML
    private Text text;

    private final boolean isBlanc;
    private final Position position;
    private final CaseClickListener caseClickListener;

    public CaseController(Position position, CaseClickListener caseClickListener, boolean isBlanc) {
        this.isBlanc = isBlanc;
        this.position = position;
        this.caseClickListener = caseClickListener;
    }

    @FXML
    private void initialize() {
        //Met la couleur blanc ou gris
        setHighlight(false);
        root.heightProperty().addListener((observable, oldValue, newValue) -> text.setFont(Font.font(newValue.floatValue() * FONT_TO_HEIGHT_RATIO)));
        root.setOnMouseClicked(event -> caseClickListener.caseClicked(position));
    }

    void setPiece(Piece piece) {
        if (piece == null) text.setText(null);
        else text.setText(piece.getUnicode());
    }

    void setHighlight(boolean isHighlighted) {
        Color result;

        if (isHighlighted) {
            result = isBlanc ? Color.LIGHTBLUE : Color.BLUE;
        } else {
            result = isBlanc ? Color.WHITE : Color.LIGHTGRAY;
        }

        root.setBackground(new Background(new BackgroundFill(result, null, null)));
    }

}
