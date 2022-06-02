package com.example.forecast;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class HelloController {
    @FXML
    private TextField a;
    private LineChart lineChart;

    @FXML
    protected void onHelloButtonClick() {
        a.setText("GACHI!");
    }
    @FXML
    protected void Graph(){
        
    }
}
