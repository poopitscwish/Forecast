package com.example.forecast;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    @FXML
    private Desktop desktop = Desktop.getDesktop();

    private ArrayList<Double> curs;
    String textArea;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Graph("G:\\data\\data.xlsx");
    }
//Отрисовка графика
    public void Graph(String path/*path это путь к файлу который берется из OpenFile в 99 строчке */){
        text.getChildren().clear();//Очистка полей
        chart1.getData().clear();

        XYChart.Series<String, Number> series1 = new XYChart.Series<>();//серия значений
        series1.setName("USD");

        try {
            Parsing USD = new Parsing(path);//
            USD.read();
            chart1.setCreateSymbols(false);
            this.curs = new ArrayList<>(USD.getData());//Инициализирую массив курса доллара
            for (int i = 0; i < curs.size(); i++) {
                series1.getData().add(new XYChart.Data<>(USD.getTime().get(i), curs.get(i))); //Здесь добовляю к серии значений по Х - даты по У - курс
            }
            chart1.getData().add(series1);// добовляю на lInechart серии значений
            /*ObservableList<XYChart.Data> dataList = ((XYChart.Series) chart1.getData().get(0)).getData();
            dataList.forEach(data -> {
                Node node = data.getNode();
                Tooltip tooltip = new Tooltip('(' + data.getXValue().toString() + ';' + data.getYValue().toString() + ')');  //Этот код должен был довать подсказки при наведении на значения но почему то не работает. Ссылка откуда брал информацию URL: https://habr.com/ru/post/242009/
                Tooltip.install(node, tooltip);
            });*/
            Statistics stat = new Statistics(curs);// здесь статистика
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



//Открытие фала по нажатию на кнопку меню 'Open' на главном экране вызывается этот метод
    public void OpneFile() {
        textArea = "";
        final FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            openFile(file);
            List<File> files = Arrays.asList(file);
            printLog(files);
        }
        System.out.println(textArea);
        Graph(textArea);
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
            textArea +=file.getAbsolutePath();
        }
    }
    private void openFile(File file) {
        try {
            this.desktop.open(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//Открытие файла


    //Прогнозирование
    public void predict() {
        text2.getChildren().clear();
        chart2.getData().clear();
        Statistics stat = new Statistics(curs);

        //stat.getMean(); // DoubleSummaryStatistics{count=96, sum=7743,185900, min=56,299600, average=80,658186, max=120,378500} В этой ячейке вот что хранится, через . можно обращаться к каждому отдельно пример stat.getMean().getCount() - вернет 96
        //Здесь я пытался решить систему уравнений
        float sum_times = 0;
        int count = (int) stat.getMean().getCount();//count
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
        chart2.setCreateSymbols(false);
        float a_param = (float) ((count * sum_times - sumX * sumY) / (count * sumX2 - Math.pow(sumX, 2)));
        float b_param = (float) (sumY - a_param * sumX) / count;
        text2.getChildren().add(new TextFlow(new Text(String.format(
                "y=%sx%s", a_param, b_param)
        )));
        System.out.println(a_param + "\n" + b_param);

        //Отрисовка второго графика
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("USD");
        for (int k = 0; k < curs.size(); k++) {
            series1.getData().add(new XYChart.Data<>(Integer.toString(k), curs.get(k)));
        }
        chart2.getData().add(series1);
        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        series2.setName("Predict");
        for(int k = 0; k < curs.size() + 10; k++) {
        series2.getData().add(new XYChart.Data<>(Integer.toString(k), a_param * k + b_param));
        }
        chart2.getData().add(series2);
    }
}
