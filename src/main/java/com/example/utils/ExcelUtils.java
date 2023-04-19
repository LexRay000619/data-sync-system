package com.example.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Lex
 */
public class ExcelUtils {
    public static void convertStringToExcel(String excelString, String path) {
        try {
            // Convert string to JSONArray
            JSONArray jsonArray = new JSONArray(excelString);
            // Get the headers from the first row
            JSONArray headers = jsonArray.getJSONArray(0);
            // Create a new workbook
            XSSFWorkbook workbook = new XSSFWorkbook();
            // Create a new sheet
            XSSFSheet sheet = workbook.createSheet("Sheet1");
            // Add headers to the sheet
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers.getString(i));
            }
            // Add data rows to the sheet
            for (int i = 1; i < jsonArray.length(); i++) {
                JSONArray rowData = jsonArray.getJSONArray(i);
                Row row = sheet.createRow(i);
                for (int j = 0; j < rowData.length(); j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellValue(rowData.getString(j));
                }
            }
            // Write the workbook to a file
            FileOutputStream fileOut = new FileOutputStream(new File(path));
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();
            System.out.println("Excel file created and saved at " + path);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }
}
