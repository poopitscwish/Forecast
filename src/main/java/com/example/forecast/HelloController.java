package com.example.forecast;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

import javafx.scene.control.Tooltip;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class HelloController implements Initializable {
    @FXML
    LineChart chart1;
    @FXML
    LineChart chart2;
    @FXML
    TextFlow text;
    @FXML
    TextFlow text2;

    private ArrayList<Double> curs;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        XYChart.Series<Number, Number> series1 = new XYChart.Series<>();
        series1.setName("USD");
        try {
            Parsing USD = new Parsing("G:\\data\\", "data.xlsx");
            USD.read();
            this.curs = new ArrayList<>(USD.getData());
            for (int i = 0; i < curs.size(); i++) {
                series1.getData().add(new XYChart.Data<>(i, curs.get(i)));
            }
            chart1.getData().add(series1);
            ObservableList<XYChart.Data> dataList = ((XYChart.Series) chart1.getData().get(0)).getData();
            dataList.forEach(data -> {
                Node node = data.getNode();
                Tooltip tooltip = new Tooltip('(' + data.getXValue().toString() + ';' + data.getYValue().toString() + ')');
                Tooltip.install(node, tooltip);
            });

            Statistics stat = new Statistics(curs);
            System.out.println(stat.getMean());
            text.getChildren().add(new TextFlow(new Text(String.format(
                    "Статистика:\nКоличество:%s\nСумма:%s\nМинимум:%s\nМаксимум:%s\nСреднее:%s\nДисперсия:%s\nСреднее откланение:%s\nМедиана:%s",
                    stat.getMean().getCount(), (float) stat.getMean().getSum(),
                    stat.getMean().getMin(), stat.getMean().getMax(),
                    (float) stat.getMean().getAverage(),
                    (float) stat.getVariance(),
                    (float) stat.getStdDev(),
                    stat.median()))));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //y=at+b
    public void predict() {
        Statistics stat = new Statistics(curs);
        float sum_times = 0;
        int count = (int) stat.getMean().getCount();
        int sumX = 0;//sumX
        float sum = (float) stat.getMean().getSum();//sumY
        double sumY = 0; //sumY
        int sumX2 = 0;
        int i = 1;
        for (Object a : curs) {
            sumY += (double) a;
            sumX2 += Math.pow(i, 2);
            i++;
            sumX += i;
            sum_times += sumX * sumY;
        }
        double[][] n = {{sumX2, sumX, sum_times},
                {sumX, count, sum}};
        for (double[] a : n) {
            for (double b : a) {
                System.out.print(b + "  ");
            }
            System.out.println();
        }
        float a_param =(float)( (count * sum_times - sumX * sumY) / (count * sumX2 - Math.pow(sumX, 2)));
        float b_param =(float) (sumY - a_param * sumX) / count;
        text2.getChildren().add(new TextFlow(new Text(String.format(
                "y=%sx%s",a_param,b_param)
        )));
        System.out.println(a_param + "\n" + b_param);
        XYChart.Series<Number, Number> series1 = new XYChart.Series<>();
        series1.setName("USD");
        for (int k = 0; k < curs.size(); k++) {
            series1.getData().add(new XYChart.Data<>(k, curs.get(k)));
        }
        chart2.getData().add(series1);
        XYChart.Series<Number, Number> series2 = new XYChart.Series<>();
        series2.setName("Predict");
        for (int k = 0; k < curs.size()+10; k++) {
            series2.getData().add(new XYChart.Data<>(k, a_param*k+b_param));
        }
        chart2.getData().add(series2);
    }
}
