package com.example.forecast;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.io.IOException;

public class ForeCast extends Application {
    private int HEIGHT = 720;
    private int WIDTH = 720;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ForeCast.class.getResource("hello-view.fxml"));

        Group group = new Group();
        group.getChildren().addAll(
                new Line(0,HEIGHT/2, WIDTH,HEIGHT/2),
                new Line(HEIGHT/2,0, WIDTH/2,HEIGHT)
        );

        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle("Forecast");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}