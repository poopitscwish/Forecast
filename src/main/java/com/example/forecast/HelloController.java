package com.example.forecast;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import org.javamoney.moneta.FastMoney;
import org.javamoney.moneta.Money;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.convert.CurrencyConversion;
import javax.money.convert.ExchangeRateProvider;
import javax.money.convert.MonetaryConversions;
import java.io.Console;
import java.net.URL;
import java.time.Month;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.ResourceBundle;



public class HelloController implements Initializable  {
@FXML LineChart chart1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        XYChart.Series<Number, Number> series1 = new XYChart.Series<>();
        series1.setName("USD");
        try {
            Parsing USD = new Parsing("G:\\data\\", "data.xlsx");
            USD.read();
            ArrayList<Double> curs = new ArrayList<>(USD.getData());
            for(int i = 0; i<curs.size();i++) {
                series1.getData().add(new XYChart.Data<>(i, curs.get(i)));
            }
            chart1.getData().add(series1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
