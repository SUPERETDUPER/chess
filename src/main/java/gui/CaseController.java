package gui;

import javafx.fxml.FXML;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import modele.pieces.Piece;
import modele.plateau.Position;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * Controle une case
 */
class CaseController {
    enum Highlight {
        NORMAL,
        ROUGE,
        BLUE
    }

    private static final float FONT_TO_HEIGHT_RATIO = 0.75F;

    @FXML
    private StackPane root; //La boite qui contient le text

    @FXML
    private Text text; //Le text

    private final boolean isBlanc;
    @NotNull
    private final Position position;

    @NotNull
    private final Consumer<Position> caseClickListener;

    /**
     * @param position          la position de la case
     * @param caseClickListener le listener à appeler quand la case est appuyé
     * @param isBlanc           si la case est blanche
     */
    CaseController(@NotNull Position position, @NotNull Consumer<Position> caseClickListener, boolean isBlanc) {
        this.isBlanc = isBlanc;
        this.position = position;
        this.caseClickListener = caseClickListener;
    }

    @FXML
    private void initialize() {
        setCouleur(Highlight.NORMAL);  //Met la couleur de l'arrière plan de la case

        //Bind la taille du text à la hauteur de la case
        root.heightProperty().addListener(
                (observable, oldValue, newValue) ->
                        text.setFont(Font.font(newValue.floatValue() * FONT_TO_HEIGHT_RATIO))
        );
    }

    @FXML
    private void handleClick() {
        caseClickListener.accept(position);
    }

    /**
     * @param piece la piece à montrer
     */
    void setPiece(@Nullable Piece piece) {
        if (piece == null) text.setText(null);
        else text.setText(piece.getUnicode());
    }

    /**
     * @param highlight la nouvelle couleur de l'arrière plan de l'arrière plan
     */
    void setCouleur(@NotNull Highlight highlight) {
        root.setBackground(new Background(new BackgroundFill(getCouleur(highlight), null, null)));
    }

    private Color getCouleur(@NotNull Highlight highlight) {
        switch (highlight) {
            case BLUE:
                return isBlanc ? Color.LIGHTBLUE : Color.CORNFLOWERBLUE;
            case ROUGE:
                return Color.PALEVIOLETRED;
            default:
                return isBlanc ? Color.WHITE : Color.LIGHTGRAY;
        }
    }

    @NotNull
    public Position getPosition() {
        return position;
    }
}
