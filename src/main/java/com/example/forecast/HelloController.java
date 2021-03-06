package com.example.forecast;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.chart.XYChart;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;

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
    private LineChart<NumberAxis, NumberAxis> chart1;

    @FXML
    private TextFlow text;

    @FXML
    private TextFlow text2;
    static int numerator = 1;
    private XYChart.Series<Number, Number> series;
    private XYChart.Series<Number, Number> series2;
    private ArrayList<Double> curs;
    String textArea;
    Parsing parsing;
    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();

    @FXML
    void initialize() {
        assert Open != null : "fx:id=\"Open\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert X1 != null : "fx:id=\"X1\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Y1 != null : "fx:id=\"Y1\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert chart1 != null : "fx:id=\"chart1\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert text != null : "fx:id=\"text\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert text2 != null : "fx:id=\"text2\" was not injected: check your FXML file 'hello-view.fxml'.";
        chart1.setData(FXCollections.observableArrayList(new XYChart.Series("USD", plot(1, 2, 3, 4, 5))));
        chart1.getStylesheets().add(getClass().getResource("mycss.css").toExternalForm());
    }

    public void EnterData() {
        String a = JOptionPane.showInputDialog("?????????????? ???????????? ?????????? ??????????????");
        String[] result = a.split(",");
        ArrayList<Double> data = new ArrayList<>();
        series = new XYChart.Series<>();
        try {
            for (int i = 0; i < result.length; i++) {
                data.add(Double.parseDouble(result[i]));
                series.getData().add(new XYChart.Data<>(i, data.get(i)));
            }
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("??????????");
            alert.setHeaderText("?????????????? ?????????? ?????????????");
            alert.setContentText("?????????????? ???? ?????? ?????????????? ?? Cancel ?????? ????????????");
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get() == null || option.get() == ButtonType.CANCEL) {
                chart1.getData().clear();
                text2.getChildren().clear();
                curs = new ArrayList<>(data);
                Graph(series);
            } else if (option.get() == ButtonType.OK) {
                Graph(series);
            }
            numerator++;
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("???????? ?????????????? ???????????????????????? ????????????????");
            alert.showAndWait();
            EnterData();

        }
    }

    public void SetPoint() {
        ChoiceDialog<XYChart.Series> dialog = new ChoiceDialog<>();
        dialog.setContentText("?? ???????????? ?????????????? ?????????????????");
        dialog.setHeaderText(null);
        for (var item : chart1.getData()) {
            dialog.getItems().add(item);
        }
        Optional<XYChart.Series> s = dialog.showAndWait();
        JFrame frame = new JFrame();
        Container container = frame.getContentPane();
        container.setLayout(null);
        JLabel X = new JLabel("X:");
        JLabel Y = new JLabel("Y:");
        X.setBounds(3, 5, 15, 25);
        Y.setBounds(3, 35, 15, 25);
        container.add(X);
        container.add(Y);
        frame.setBounds((dimension.width - 200) / 2, (dimension.height - 150) / 2, 200, 150);
        JTextField x = new JTextField("");
        x.setBounds(20, 5, 150, 30);
        JTextField y = new JTextField("");
        y.setBounds(20, 35, 150, 30);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(true);
        container.add(x);
        container.add(y);
        JButton add = new JButton("????????????????");
        add.setBounds(40, 70, 100, 30);
        container.add(add);
        try {

            add.addActionListener(e -> {
                double _X = Double.parseDouble(x.getText());
                double _Y = Double.parseDouble(y.getText());
                s.get().getData().add(new XYChart.Data<>(_X, _Y));
            });
            Update();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("?????????????? ??????????");
            alert.showAndWait();
        }
    }

    public void Delete() {
        ChoiceDialog<XYChart.Series> dialog = new ChoiceDialog<>();
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        dialog.setContentText("???????????????? ????????????");
        dialog.setHeaderText(null);
        for (var item : chart1.getData()) {
            dialog.getItems().add(item);
        }
        Optional<XYChart.Series> ser = dialog.showAndWait();
        String a = JOptionPane.showInputDialog("?????????????? X:");
        try {
            int index = Integer.parseInt(a);
            ser.get().getData().remove(index);
            Update();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void GetData(String path) {
        try {
            text.getChildren().clear();
            parsing = new Parsing(path);
            parsing.read();
            this.curs = new ArrayList<>(parsing.getData());
            float[] a = new float[curs.size()];
            float t = 0;
            for (int i = 0; i < curs.size(); i++) {
                t = Float.parseFloat(curs.get(i).toString());
                a[i] = t;
            }
            chart1.setCursor(Cursor.CROSSHAIR);
            series = new XYChart.Series<>();
            for (int i = 0; i < curs.size(); i++) {
                series.getData().add(new XYChart.Data<>(i, curs.get(i)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Stat() {
        Statistics stat = new Statistics(curs);
        System.out.println(stat.getMean());
        text.getChildren().add(new TextFlow(new Text(String.format(
                "????????????????????:\n????????????????????:%s\n??????????:%s\n??????????????:%s\n????????????????:%s\n??????????????:%s\n??????????????????:%s\n?????????????? ????????????????????:%.3f\n??????????????:%.3f",
                stat.getMean().getCount(), (float) stat.getMean().getSum(),
                stat.getMean().getMin(), stat.getMean().getMax(),
                (float) stat.getMean().getAverage(),
                (float) stat.getVariance(),
                (float) stat.getStdDev(),
                stat.median()))));
    }// ?????????? ????????????????????

    public void Update() {
        String result = "";
        curs.clear();
        try {
            text.getChildren().clear();
            for (var s : chart1.getData())
                for (XYChart.Data<NumberAxis, NumberAxis> entry : s.getData()) {
                    result = String.format("(%s ; %.2f)", entry.getXValue(), entry.getYValue());
                    Tooltip t = new Tooltip(result);
                    Tooltip.install(entry.getNode(), t);
                    curs.add(Double.parseDouble(String.valueOf(entry.getYValue())));
                }
            Stat();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //?????????????????? ??????????????
    public void Graph(XYChart.Series series) {
        if (series.getName() == null) {
            series.setName("Graph" + numerator);
            numerator++;
        }
        try {
            chart1.getData().add(series);// ???????????????? ???? lInechart ?????????? ????????????????
            Update();
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
                            (i == 0) ? 0 : y[i - 1],
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

    //???????????????? ???????? ???? ?????????????? ???? ???????????? ???????? 'Open' ???? ?????????????? ???????????? ???????????????????? ???????? ??????????
    public void OpenFile() {
        final FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            textArea = "";
            List<File> files = Arrays.asList(file);
            printLog(files);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("?????????????? ?????????? ?????????????");
            alert.setHeaderText("???? ?????????????????????????? ???????????? ?????????????? ?????????? ?????????????");
            alert.setContentText("?????????????? ???? ?????? ?????????????? ?? Cancel ?????? ????????????");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get() == null || option.get() == ButtonType.CANCEL) {
                GetData(textArea);
                Graph(series);
            } else if (option.get() == ButtonType.OK) {
                chart1.getData().clear();
                text2.getChildren().clear();
                GetData(textArea);
                Graph(series);
            }
            System.out.println(textArea);
        }

    }

    private void printLog(List<File> files) {
        if (files == null || files.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("????????????");
            alert.showAndWait();
        }
        for (File file : files) {
            textArea += file.getAbsolutePath();
        }
    }

//???????????????? ??????????


    //??????????????????????????????
    public void predict() {
        text2.getChildren().clear();
        if (series2 != null)
            chart1.getData().remove(series2);
        int line = 0;
        String a = JOptionPane.showInputDialog("?????????????? ?????????????????? ???????????????? ?????????? ??????????????????");
        try {
            line = Integer.parseInt(a);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("?????????????? ??????????");
            alert.showAndWait();
        }

        //?????????? ?? ?????????????? ???????????? ?????????????? ??????????????????
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
        //?????????????????? ?????????????? ??????????????
        series2.setName("Predict");
        Text text = new Text();
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < curs.size(); i++) {
            text2.getChildren().add(new Text(i + ") " + parsing.getTime().get(i) + " - " + curs.get(i) + "\n"));
        }
        text.setText(str.toString());
        text2.getChildren().add(text);
        str.delete(0, str.length());
        for (int k = 0; k < curs.size() + line; k++) {
            series2.getData().add(new XYChart.Data<>(k, _Y[k]));
            if (k >= curs.size())
                str.append(k + ")" + _Y[k] + "\n");
        }
        text.setFill(javafx.scene.paint.Color.RED);
        text.setText(str.toString());
        series2.setName(String.format("y=%.2f*(x-%.2f)+%s", b1, _y, _x));
        try {
            Graph(series2);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void Help() {
        TextFlow textflow = new TextFlow();
        Text help = new Text();
        help.setText(String.format("Forecast - ?????? ?????????????????? ?????? ???????????????????? ???????????????? ?????????????????? ???????????? ?? ?????????? ?????? ?????????????? ??????????, ?????????????????????????????? ?????????????? ?? ?????????????????????????????? ???? ???????????? ?????????? ????????????????.\n" +
                "???????????? ?????????????????? ???????????????????? ?????????????????? ?? ???????????????? ?? ?????????????????? ???? ??????????????????????: ?????????????????? ??????????????, ?????????????????? ?????? ?????????????? ?????????? ?????????????????? ????????????????."));
        textflow.getChildren().add(help);
        StackPane secondaryLayout = new StackPane();
        secondaryLayout.getChildren().add(textflow);
        int w = 300;
        int h = 300;
        Scene secondScene = new Scene(secondaryLayout, w, h);
        // New window (Stage)
        Stage newWindow = new Stage();
        newWindow.setTitle("?? ??????????????????");
        newWindow.setScene(secondScene);

        // Set position of second window, related to primary window.
        newWindow.setX((dimension.width-w) / 2);
        newWindow.setY((dimension.height-h) / 2);

        newWindow.show();
    }
}