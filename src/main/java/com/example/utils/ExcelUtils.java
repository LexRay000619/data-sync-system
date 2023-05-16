package com.example.utils;

import com.google.gson.Gson;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.WritableResource;

import java.io.*;
import java.util.*;


/**
 * @author Lex
 */
public class ExcelUtils {
    static Log log = LogFactory.getLog(ExcelUtils.class);

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
            WritableResource resource = new FileSystemResource(path);
            OutputStream fileOut = resource.getOutputStream();
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();
            log.info("Excel file created and saved at " + path);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    public static String excelToJson(String filePath) throws IOException {
        Resource resource = new FileSystemResource(filePath);
        InputStream inputStream = resource.getInputStream();
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        List<List<String>> data = new ArrayList<>();
        Row header = sheet.getRow(0);
        int numColumns = header.getLastCellNum();
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            List<String> rowData = new ArrayList<>();
            for (int j = 0; j < numColumns; j++) {
                Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                String cellValue = cell.toString();
                rowData.add(cellValue);
            }
            data.add(rowData);
        }
        return new Gson().toJson(data);
    }
}
