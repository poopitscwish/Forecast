package com.example.forecast;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.File;
import java.net.URL;
import java.util.*;


public class HelloController implements Initializable {
    @FXML
    NumberAxis X1;
    @FXML
    NumberAxis Y1;
    @FXML
    LineChart chart1;
    @FXML
    TextFlow text;
    @FXML
    TextFlow text2;
    @FXML
    private Desktop desktop = Desktop.getDesktop();


    private XYChart.Series<Number, Number> series;
    private ArrayList<Double> curs;
    String textArea;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Graph("G:\\data\\RC_F01_05_2022_T01_06_2022.xlsx");
        text.getChildren().clear();//Очистка полей
        text2.getChildren().clear();
        chart1.getData().clear();
    }

    public void GetData(String path) {
        try {
            text.getChildren().clear();
            Parsing a = new Parsing(path);
            a.read();
            this.curs = new ArrayList<>(a.getData());
            this.series = new XYChart.Series();
            for (int i = 0; i < curs.size(); i++) {
                series.getData().add(new XYChart.Data<>(i, curs.get(i))); //Здесь добавляю к серии значений по Х - даты по У - курс
                text2.getChildren().add(new Text(i + ") " + a.getTime().get(i) + " - " + curs.get(i) + "\n"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Отрисовка графика
    public void Graph(XYChart.Series series) {
        chart1.getData().clear();
        series.setName("USD");
        try {
            chart1.setCreateSymbols(true);
            //chart1.getStylesheets().add(ForeCast.class.getResource("G:\\asd.css").toExternalForm());
            chart1.getData().addAll(series);// добовляю на lInechart серии значений
            ObservableList<XYChart.Data> dataList = ((XYChart.Series)   chart1.getData().get(0)  ).getData();
            dataList.forEach(data -> {
                Node node = data.getNode();
                Tooltip tooltip = new Tooltip('(' + data.getXValue().toString() + ';' + data.getYValue().toString() + ')');  //Этот код должен был давать подсказки при наведении на значения, но почему-то не работает. Ссылка откуда брал информацию URL: https://habr.com/ru/post/242009/
                Tooltip.install(node, tooltip);
            });
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //Открытие фала по нажатию на кнопку меню 'Open' на главном экране вызывается этот метод
    public void OpneFile() {
        textArea = "";
        final FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            //openFile(file);
            List<File> files = Arrays.asList(file);
            printLog(files);
        }
        System.out.println(textArea);
        GetData(textArea);
        Graph(series);
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
        Statistics stat = new Statistics(curs);
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
        double[] _Y = new double[curs.size()];
        for (int i = 0; i < _Y.length; i++) {
            _Y[i] = _y + b1 * (x[i] - _x);
        }


        //Отрисовка второго графика
        series.setName("USD");
        XYChart.Series<Number, Number> series2 = new XYChart.Series<>();
        series2.setName("Predict");
        for (int k = 0; k < curs.size(); k++) {
            series2.getData().add(new XYChart.Data<>(k, _Y[k]));
        }
        chart1.getData().addAll(series2);
    }
}
