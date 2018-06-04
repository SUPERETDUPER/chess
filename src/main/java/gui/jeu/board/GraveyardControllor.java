package gui.jeu.board;

import gui.jeu.board.view.PiecePane;
import javafx.scene.layout.Pane;
import modele.pieces.Couleur;

public class GraveyardControllor {
    private final DisplayCalculator displayCalculator;
    private final Pane container;

    public GraveyardControllor(DisplayCalculator displayCalculator, Pane container) {
        this.displayCalculator = displayCalculator;
        this.container = container;
    }

    void addPiece(PiecePane piecePane) {
        Couleur couleur = piecePane.getPiece().getCouleur();

        if (couleur == Couleur.BLANC) {
            displayCalculator.incrementGraveyardBlanc();
        } else {
            displayCalculator.incrementGraveyardNoir();
        }


        piecePane.layoutXProperty().bind(displayCalculator.getGraveyardX(couleur));
        piecePane.layoutYProperty().bind(displayCalculator.getGraveyardY(couleur));
        container.getChildren().add(piecePane);
    }
}
