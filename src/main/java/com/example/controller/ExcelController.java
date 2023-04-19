package com.example.controller;

import com.example.utils.ExcelUtils;
import org.springframework.web.bind.annotation.*;

import java.io.*;

/**
 * @author Lex
 */
@CrossOrigin(origins = {"*"})
@RestController
public class ExcelController {
    @RequestMapping("/save")
    public String excel(@RequestBody String string) {
        String filename = "src/main/resources/static/excels/serverExcel.xlsx";
        ExcelUtils.convertStringToExcel(string, filename);
        return "The Excel file is successfully saved on the server side.";
    }

    @GetMapping("/excel")
    public String getString() throws IOException {
        System.out.println("JSON string has been successfully sent to the client.");
        return ExcelUtils.excelToJson("src/main/resources/static/excels/serverExcel.xlsx");
    }
}
