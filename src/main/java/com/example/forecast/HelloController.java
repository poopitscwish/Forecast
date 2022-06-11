package com.example.forecast;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;


public class HelloController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private MenuItem Open;

    @FXML
    private NumberAxis X1;

    @FXML
    private NumberAxis Y1;

    @FXML
    private LineChart<NumberAxis,NumberAxis> chart1;

    @FXML
    private TextFlow text;

    @FXML
    private TextFlow text2;

    private XYChart.Series<Number, Number> series;
    private XYChart.Series<Number, Number> series2;
    private ArrayList<Double> curs;
    String textArea;
    Parsing parsing;

    @FXML
    void initialize() {
        assert Open != null : "fx:id=\"Open\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert X1 != null : "fx:id=\"X1\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Y1 != null : "fx:id=\"Y1\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert chart1 != null : "fx:id=\"chart1\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert text != null : "fx:id=\"text\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert text2 != null : "fx:id=\"text2\" was not injected: check your FXML file 'hello-view.fxml'.";
        chart1.setData(FXCollections.observableArrayList(new XYChart.Series("USD", plot(1,2,3,4,5))));
    }

    public void GetData(String path) {
        try {
            text.getChildren().clear();
            parsing = new Parsing(path);
            parsing.read();
            this.curs = new ArrayList<>(parsing.getData());
            float[] a = new float[curs.size()];
            float t = 0;
            for(int i = 0; i<curs.size();i++){
                t =Float.parseFloat(curs.get(i).toString());
                a[i] = t;
            }
            chart1.setCursor(Cursor.CROSSHAIR);
            series = new XYChart.Series<>();
            for(int i = 0; i<curs.size();i++){
                series.getData().add(new XYChart.Data<>(i,curs.get(i)));
            }
            //chart1.setData(FXCollections.observableArrayList(new XYChart.Series("USD", plot(a))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Stat(){
        Statistics stat = new Statistics(curs);// здесь статистика
        System.out.println(stat.getMean());
        text.getChildren().add(new TextFlow(new Text(String.format(
                "Статистика:\nКоличество:%s\nСумма:%s\nМинимум:%s\nМаксимум:%s\nСреднее:%s\nДисперсия:%s\nСреднее отклонение:%.3f\nМедиана:%.3f",
                stat.getMean().getCount(), (float) stat.getMean().getSum(),
                stat.getMean().getMin(), stat.getMean().getMax(),
                (float) stat.getMean().getAverage(),
                (float) stat.getVariance(),
                (float) stat.getStdDev(),
                stat.median()))));
    }
    //Отрисовка графика
    public void Graph(XYChart.Series series, String name, int code) {
        series.setName(name);
        try {
            chart1.getData().add(series);// добовляю на lInechart серии значений
            for(var s : chart1.getData())
            for (XYChart.Data<NumberAxis,NumberAxis> entry: s.getData()) {
                Tooltip t = new Tooltip(String.format("%.2f",entry.getYValue()));
                Tooltip.install(entry.getNode(), t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ObservableList<XYChart.Data<Integer, Float>> plot(float... y) {
        final ObservableList<XYChart.Data<Integer, Float>> dataset = FXCollections.observableArrayList();
        int i = 0;
        while (i < y.length) {
            final XYChart.Data<Integer, Float> data = new XYChart.Data<>(i + 1, y[i]);
            data.setNode(
                    new HoveredThresholdNode(
                            (i == 0) ? 0 : y[i-1],
                            y[i]
                    )
            );

            dataset.add(data);
            i++;
        }

        return dataset;
    }
    class HoveredThresholdNode extends StackPane {
        HoveredThresholdNode(float priorValue, float value) {
            setPrefSize(15, 15);

            final Label label = createDataThresholdLabel(priorValue, value);

            setOnMouseEntered(mouseEvent -> {
                getChildren().setAll(label);
                setCursor(Cursor.NONE);
                toFront();
            });
            setOnMouseExited(mouseEvent -> {
                getChildren().clear();
                setCursor(Cursor.CROSSHAIR);
            });
        }
        private Label createDataThresholdLabel(float priorValue, float value) {
            final Label label = new Label(value + "");
            label.getStyleClass().addAll("default-color0", "chart-line-symbol", "chart-series-line");
            label.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");

            if (priorValue == 0) {
                label.setTextFill(Color.DARKGRAY);
            } else if (value > priorValue) {
                label.setTextFill(Color.FORESTGREEN);
            } else {
                label.setTextFill(Color.FIREBRICK);
            }

            label.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
            return label;
        }
    }
    //Открытие фала по нажатию на кнопку меню 'Open' на главном экране вызывается этот метод
    public void OpenFile() {
        chart1.getData().clear();
        textArea = "";
        text2.getChildren().clear();
        final FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            //openFile(file);
            List<File> files = Arrays.asList(file);
            printLog(files);
        }
        System.out.println(textArea);
        GetData(textArea);
        Graph(series, "USD",0);
        Stat();
    }

    private void printLog(List<File> files) {
        if (files == null || files.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Ошибка");
            alert.showAndWait();
        }
        for (File file : files) {
            textArea += file.getAbsolutePath();
        }
    }

    /*private void openFile(File file) {
        try {
            //this.desktop.open(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
//Открытие файла


    //Прогнозирование
    public void predict() {
        text2.getChildren().clear();
        Statistics stat = new Statistics(curs);
        if (series2 != null)
            chart1.getData().remove(series2);
        int line = 0;
        String a = JOptionPane.showInputDialog("Введите насколько продлить линию регрессии");
        try {
            line = Integer.parseInt(a);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Введите числа");
            alert.showAndWait();
        }

        //Здесь я пытался решить систему уравнений
        int[] x = new int[curs.size()];
        double[] y = new double[curs.size()];
        double[] x2 = new double[curs.size()];
        double[] y2 = new double[curs.size()];
        double[] xy = new double[curs.size()];
        int sumX = 0;
        int sumX2 = 0;
        double sumY = 0;
        double sumY2 = 0;
        double sumXY = 0;

        for (int i = 0; i < x.length; i++) {
            x[i] = i;
            y[i] = curs.get(i);
            x2[i] = i * i;
            y2[i] = curs.get(i) * curs.get(i);
            xy[i] = x[i] * y[i];
            sumX += x[i];
            sumX2 += x2[i];
            sumY += y[i];
            sumY2 += y2[i];
            sumXY += xy[i];
        }
        var _x = sumX / curs.size();
        var _y = sumY / curs.size();
        var Qx = sumX2 - (sumX * sumX) / curs.size();
        var Qxy = sumXY - (sumX * sumY) / curs.size();
        var b1 = Qxy / Qx;
        float[] _Y = new float[curs.size() + line];
        for (int i = 0; i < _Y.length; i++) {
            _Y[i] = (float) (_y + b1 * (i - _x));
        }
        series2 = new XYChart.Series<>();
        //Отрисовка второго графика
        series.setName("USD");
        series2.setName("Predict");
        Text text = new Text();
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < curs.size(); i++) {
            series.getData().add(new XYChart.Data<>(i, curs.get(i))); //Здесь добавляю к серии значений по Х - даты по У - курс
            text2.getChildren().add(new Text(i + ") " + parsing.getTime().get(i) + " - " + curs.get(i) + "\n"));
        }
        text.setText(str.toString());
        text2.getChildren().add(text);
        str.delete(0, str.length());
        for (int k = 0; k < curs.size() + line; k++) {
            series2.getData().add(new XYChart.Data<>(k, _Y[k]));
            if(k>=curs.size())
                str.append(k+")"+_Y[k]+"\n");
        }
        text.setFill(javafx.scene.paint.Color.RED);
        text.setText(str.toString());
        Graph(series2,"Predict",1);
    }
}
