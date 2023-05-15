package com.example.controller;

import com.example.utils.ExcelUtils;
import com.example.utils.FileUtils;
import com.example.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.*;

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

    @Value("${file.directory}")
    private String fileDirectory;

    @GetMapping("/getExcelFileList")
    public List<String> fileList(Model model) {
        File directory = new File(fileDirectory);
        File[] files = directory.listFiles();
        // 下面这行代码似乎没起到什么作用,实际上文件名的传输是通过HTTP请求完成的
        model.addAttribute("files", files);
        System.out.println(Arrays.toString(files));
        List<String> fileNames = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    fileNames.add(file.getName());
                }
            }
        }
        return fileNames;
    }

    @PostMapping("/process-file")
    public String processFile(@RequestParam("selectedFile") String selectedFile) {
        // Process the selected file
        // You can perform any desired actions here, such as storing the file name in a variable or database.
        System.out.println("Selected file: " + selectedFile);
        return "redirect:/";
    }

}
