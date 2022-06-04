package com.example.forecast;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import java.io.Console;
import java.net.URL;
import java.time.Month;
import java.util.ArrayList;
import java.util.Currency;
import java.util.ResourceBundle;



public class HelloController implements Initializable {
@FXML LineChart chart1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        XYChart.Series<Number, Number> series1 = new XYChart.Series<>();
        series1.setName("Series 1");
        Currency usd = Currency.getInstance("USD");
        System.out.print(usd.getCurrencyCode());
        int x = 0;
        ArrayList<Double> y = new ArrayList<>();
        while(x<=10) {
            y.add(Math.pow(x, 2));
            series1.getData().add(new XYChart.Data<>(x, y.get(x)));
            x++;
        }

        chart1.getData().add(series1);
    }
}
