package com.example.forecast;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML
    private LineChart lineChart;

    @FXML
    public void initialize(URL url, ResourceBundle rb){
        XYChart.Series series = new XYChart.Series();
        series.setName("Sales");
        series.getData().add(new XYChart.Data("Jan",23));
        series.getData().add(new XYChart.Data("Feb",19));
        series.getData().add(new XYChart.Data("March",15));
        series.getData().add(new XYChart.Data("April",12));
        lineChart.getData().add(series);
    }
}
