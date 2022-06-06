package com.example.forecast;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;


public class Parsing {
    String path;
    String name;
    FileInputStream file;
    HashMap<Integer, List<Object>> data = new HashMap<>();

    public Parsing(String path, String name) {
        this.path = path;
        this.name = name;
    }

    public void read() throws IOException {
        Workbook workbook = loadWorkbook(path + name);
        var sheetIterator = workbook.sheetIterator();
        while (sheetIterator.hasNext()) {
            Sheet sheet = sheetIterator.next();
            processSheet(sheet);
            System.out.println();
        }
    }

    private Workbook loadWorkbook(String filename) throws IOException {
        var extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        var file = new FileInputStream(filename);
        switch (extension) {
            case "xls":
                // old format
                return new HSSFWorkbook(file);
            case "xlsx":
                // new format
                return new XSSFWorkbook(file);
            default:
                throw new RuntimeException("Unknown Excel file extension: " + extension);
        }
    }

    private void processSheet(Sheet sheet) {
        System.out.println("Sheet: " + sheet.getSheetName());
        var data = new HashMap<Integer, List<Object>>();
        var iterator = sheet.rowIterator();
        for (var rowIndex = 0; iterator.hasNext(); rowIndex++) {
            var row = iterator.next();
            processRow(data, rowIndex, row);
        }
        System.out.println("Sheet data:");
        System.out.println(data);
        this.data = data;
    }

    private void processRow(HashMap<Integer, List<Object>> data, int rowIndex, Row row) {
        data.put(rowIndex, new ArrayList<>());
        for (var cell : row) {
            processCell(cell, data.get(rowIndex));
        }

    }

    private void processCell(Cell cell, List<Object> dataRow) {
        switch (cell.getCellType()) {
            case STRING:
                dataRow.add(cell.getStringCellValue());
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    dataRow.add(cell.getLocalDateTimeCellValue());
                } else {
                    dataRow.add(NumberToTextConverter.toText(cell.getNumericCellValue()));
                }
                break;
            case BOOLEAN:
                dataRow.add(cell.getBooleanCellValue());
                break;
            case FORMULA:
                dataRow.add(cell.getCellFormula());
                break;
            default:
                dataRow.add(" ");
        }
    }

    public ArrayList getData() {
        ArrayList<Double> curs = new ArrayList<>();
        int k = 0;
        try {
            for (Map.Entry<Integer, List<Object>> entry : data.entrySet()) {
                if (k != 0)
                    curs.add(Double.parseDouble(entry.getValue().get(2).toString()));
                k = 1;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return sort(curs);
    }

    private ArrayList sort(ArrayList<Double> l) {
        System.out.println(l);
        ArrayList<Double> curs = new ArrayList<>();
        for (int i = l.size() - 1; i >= 0; i--) {
            curs.add(l.get(i));
        }
        return curs;
    }

}

