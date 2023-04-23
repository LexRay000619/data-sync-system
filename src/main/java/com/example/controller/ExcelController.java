package com.example.controller;

import com.example.utils.ExcelUtils;
import com.example.utils.FileUtils;
import com.example.utils.TimeUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @author Lex
 */
@CrossOrigin(origins = {"*"})
@RestController
public class ExcelController {
    @PostMapping("/saveExcelToServer")
    public String saveExcelToServer(@RequestBody String jsonString) {
        String filePath = FileUtils.getFilePath();
        ExcelUtils.convertStringToExcel(jsonString, filePath);
        return "The Excel file is successfully saved on the server side.";
    }

    @GetMapping("/getJsonStringFromServer")
    public String getJsonStringFromServer() throws Exception {
        System.out.println(TimeUtils.getCurrentTime() + "JSON string has been successfully sent to the client.");
        return ExcelUtils.excelToJson(FileUtils.getFilePath());
    }
}
