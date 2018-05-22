package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

import java.io.IOException;

/**
 * Controle le plateau de jeu
 */
public class BoardController {
    private static final int TAILLE_DU_PLATEAU = 8;

    @FXML
    private GridPane plateau;

    @FXML
    private void initialize() throws IOException {
        RowConstraints rowConstraint = new RowConstraints();
        rowConstraint.setVgrow(Priority.SOMETIMES);
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHgrow(Priority.SOMETIMES);

        //Crée une case pour chaque position
        for (int i = 0; i < TAILLE_DU_PLATEAU; i++) {
            for (int j = 0; j < TAILLE_DU_PLATEAU; j++) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/case.fxml"));
                fxmlLoader.setController(
                        new CaseController(
                                (i + j) % 2 == 0 //Calcule si la case devrait être blanche (en-haut à gauche est blanc)
                        )
                );

                plateau.add(fxmlLoader.load(), i, j);
            }

            plateau.getRowConstraints().add(rowConstraint);
            plateau.getColumnConstraints().add(columnConstraints);
        }
    }
}