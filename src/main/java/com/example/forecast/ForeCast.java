package com.example.forecast;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.javamoney.moneta.*;

import java.io.IOException;
import java.util.Currency;

public class ForeCast extends Application {
    private int HEIGHT = 720;
    private int WIDTH = 720;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ForeCast.class.getResource("hello-view.fxml"));
        stage.setTitle("Forecast");
        Money a;
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}