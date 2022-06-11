package com.example.forecast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.DoubleSummaryStatistics;

public class Statistics {
    ArrayList data;
    int size;
//Здесь статистика ничего интересного
    public Statistics(ArrayList<Double> data) {
        this.data = data;
        size = data.size();
    }

    public DoubleSummaryStatistics getMean() {
        return data.stream().mapToDouble(n -> (double) n).summaryStatistics();
    }

     public double getVariance() {
        double mean = data.stream().mapToDouble(n->(double) n).average().getAsDouble();
        double temp = data.stream().mapToDouble(n -> ( (double)n - mean) * ( (double) n - mean)).sum();
        return temp/(size-1);
    }

    public double getStdDev() {
        return Math.sqrt(getVariance());
    }

    public double median() {
        ArrayList<Double> a = new ArrayList<>(data);
        a.sort(null);

        if (a.size() % 2 == 0) {
            return  ((double) (a.get( (a.size() / 2) - 1)) + (double) (a.get(a.size() / 2) )) / 2.0;
        }
        return (double)a.get(a.size() / 2);
    }

}
