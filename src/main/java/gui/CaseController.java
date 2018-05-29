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
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * Controle une case
 */
class CaseController {
    /**
     * Les différentes couleurs possible pour la case
     */
    enum Highlight {
        NORMAL,
        ROUGE,
        BLUE
    }

    //La taille du text par rapport à la case
    private static final float FONT_TO_HEIGHT_RATIO = 0.75F;

    @FXML
    private StackPane root; //La boite qui contient le text

    @FXML
    private Text text; //Le text

    //Si la case est blanche ou noir (gris)
    private final boolean isBlanc;

    //La position de la case
    @NotNull
    private final Position position;

    //La méthode à appeler quand la case est appuyée
    @NotNull
    private final Consumer<Position> clickListener;

    /**
     * @param position      la position de la case
     * @param clickListener le listener à appeler quand la case est appuyé
     * @param isBlanc       si la case est blanche
     */
    CaseController(@NotNull Position position, @NotNull Consumer<Position> clickListener, boolean isBlanc) {
        this.isBlanc = isBlanc;
        this.position = position;
        this.clickListener = clickListener;
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
        clickListener.accept(position);
    }

    /**
     * Change la pièce sur la case
     *
     * @param piece la piece à montrer
     */
    void setPiece(@Nullable Piece piece) {
        text.setText(piece == null ? null : Character.toString((char) piece.getNumeroUnicode()));
    }

    /**
     * @param highlight la nouvelle couleur de l'arrière plan de l'arrière plan
     */
    void setCouleur(@NotNull Highlight highlight) {
        root.setBackground(new Background(new BackgroundFill(getCouleur(highlight), null, null)));
    }

    @Contract(pure = true)
    private Color getCouleur(@NotNull Highlight highlight) {
        switch (highlight) {
            case BLUE:
                return isBlanc ? Color.LIGHTBLUE : Color.CORNFLOWERBLUE;
            case ROUGE:
                return Color.PALEVIOLETRED;
            case NORMAL:
                return isBlanc ? Color.WHITE : Color.LIGHTGRAY;
            default:
                throw new IllegalArgumentException("Couleur de highlight inconnue");
        }
    }

    @NotNull
    public Position getPosition() {
        return position;
    }
}
