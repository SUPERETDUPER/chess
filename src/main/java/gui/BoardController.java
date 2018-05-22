package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.io.IOException;

public class BoardController {
    @FXML
    private GridPane board;

    @FXML
    private void initialize() throws IOException {
        RowConstraints rowConstraint = new RowConstraints();
        rowConstraint.setFillHeight(true);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("board.fxml"));

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board.add(fxmlLoader.load(), i, j);
            }

            board.getRowConstraints().add(rowConstraint);
        }
    }
}
