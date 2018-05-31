package gui.view;

import javafx.animation.Transition;
import javafx.beans.binding.NumberBinding;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import modele.pieces.Piece;

/**
 * Une pièce affichée sur l'écran
 */
public class PiecePane extends StackPane {
    private static final float RAPPORT_TAILLE_FONT_SIZE = 0.75F;
    private static int id_counter = 0;

    /**
     * La pièce qui se fait afficher
     */
    private final Piece piece;

    private int activeAnimations = 0;

    private final int id;

    /**
     * @param piece  la pièce à afficher
     * @param taille la taille de la boite
     */
    public PiecePane(Piece piece, NumberBinding taille) {
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

        this.id = id_counter++;
    }

    public Piece getPiece() {
        return piece;
    }

    //TODO Cleanup to avoid double animations and ugly code

    /**
     * Place la pièce à la position
     */
    public void bouger(NumberBinding x, NumberBinding y) {
        if (this.getLayoutX() != x.doubleValue() || this.getLayoutY() != y.doubleValue()) {
            double xDistance = x.doubleValue() - this.getLayoutX();
            double yDistance = y.doubleValue() - this.getLayoutY();
            double startX = this.getLayoutX();
            double startY = this.getLayoutY();

            Transition transition = new Transition() {
                {
                    setCycleDuration(new Duration(100));
                }

                @Override
                protected void interpolate(double frac) {
                    try {
                        PiecePane.this.setLayoutX(startX + frac * xDistance);
                        PiecePane.this.setLayoutY(startY + frac * yDistance);
                    } catch (RuntimeException e) {
                        System.out.println("Error: " + id);
                    }
                }
            };

            transition.setOnFinished(event -> {
                System.out.println("Finished transition: " + id);
                activeAnimations -= 1;
                fixer(x, y);
            });

            synchronized (this) {
                System.out.println("Starting transition: " + id);
                this.layoutXProperty().unbind();
                this.layoutYProperty().unbind();
                activeAnimations += 1;
                transition.play();
            }
        }
    }

    /**
     * Place la pièce à la position
     */
    public void fixer(NumberBinding x, NumberBinding y) {
        synchronized (this) {
            if (activeAnimations == 0) {
                System.out.println("placer piece: " + id);
                this.layoutXProperty().bind(x);
                this.layoutYProperty().bind(y);
            }
        }
    }
}