package gui.jeu.board.view;

import gui.jeu.board.placement.PositionGraphique;
import javafx.scene.CacheHint;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import modele.moves.Mouvement;
import modele.pieces.Piece;
import modele.plateau.PlateauPiece;
import modele.plateau.Position;

import java.util.Set;

/**
 * Une pièce affichée sur l'écran
 */
public class PiecePane extends Piece {
    private static final float RAPPORT_TAILLE_FONT_SIZE = 0.75F;

    /**
     * La pièce qui se fait afficher
     */
    private final Piece piece;

    private final StackPane pane = new StackPane();

    /**
     * @param piece la pièce à afficher
     */
    public PiecePane(Piece piece, PositionGraphique position) {
        super(piece.getCouleur());

        this.piece = piece;

        //Attacher la displayCalculator
        pane.prefHeightProperty().bind(position.getTaille());
        pane.prefWidthProperty().bind(position.getTaille());
        bind(position);

        //Ajouter le text
        Text text = new Text(Character.toString((char) piece.getNumeroUnicode()));
        pane.getChildren().add(text);

        //Faire que la displayCalculator du text reste propertionelle
        pane.prefWidthProperty().addListener(
                (observable, oldValue, newValue) ->
                        text.setFont(new Font(newValue.doubleValue() * RAPPORT_TAILLE_FONT_SIZE))
        );
    }

    public StackPane getPane() {
        return pane;
    }

    /**
     * Place la pièce à la position
     */
    public void bind(PositionGraphique position) {
        pane.layoutXProperty().bind(position.getX());
        pane.layoutYProperty().bind(position.getY());
        position.notifyPlaced();
        pane.setCacheHint(CacheHint.DEFAULT);
    }

    public void unBind() {
        pane.setCacheHint(CacheHint.SPEED);
        pane.layoutXProperty().unbind();
        pane.layoutYProperty().unbind();
    }

    public boolean isAtPosition(PositionGraphique position) {
        return pane.layoutXProperty().isBound() &&
                pane.layoutYProperty().isBound() &&
                pane.getLayoutX() == position.getX().getValue().doubleValue() &&
                pane.getLayoutY() == position.getY().getValue().doubleValue();
    }

    @Override
    public Set<Mouvement> generateAllMoves(PlateauPiece plateau) {
        return piece.generateAllMoves(plateau);
    }

    @Override
    public boolean attaquePosition(PlateauPiece plateau, Position position) {
        return piece.attaquePosition(plateau, position);
    }

    @Override
    public int getValeurPositive() {
        return piece.getValeurPositive();
    }

    @Override
    public void notifyMoveCompleted(Mouvement mouvement) {
        piece.notifyMoveCompleted(mouvement);
    }

    @Override
    public void notifyMoveUndo(Mouvement mouvement) {
        piece.notifyMoveUndo(mouvement);
    }

    @Override
    public int unicodeForWhite() {
        return piece.unicodeForWhite();
    }

    @Override
    public int unicodeForBlack() {
        return piece.unicodeForBlack();
    }

    @Override
    public int hashCode() {
        return piece.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return piece.equals(obj);
    }
}