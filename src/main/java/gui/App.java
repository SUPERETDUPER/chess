package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Échec et Mat");

        URL location = getClass().getResource("/board.fxml");
        primaryStage.setScene(new Scene(new FXMLLoader(location).load()));
        primaryStage.show();
    }
}
