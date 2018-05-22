package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

import java.io.IOException;

public class BoardController {
    @FXML
    private GridPane board;

    @FXML
    private void initialize() throws IOException {
        RowConstraints rowConstraint = new RowConstraints();
        rowConstraint.setVgrow(Priority.SOMETIMES);
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHgrow(Priority.SOMETIMES);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board.add(FXMLLoader.load(getClass().getResource("/case.fxml")), i, j);
            }

            board.getRowConstraints().add(rowConstraint);
            board.getColumnConstraints().add(columnConstraints);
        }
    }
}
