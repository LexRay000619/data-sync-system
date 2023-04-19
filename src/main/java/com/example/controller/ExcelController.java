package com.example.controller;

import com.example.utils.ExcelUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @author Lex
 */
@CrossOrigin(origins = {"*"})
@RestController
public class ExcelController {
    @RequestMapping("/test")
    public String excel(@RequestBody String string) {
        System.out.println(string);
        String filename = "D:\\文件\\课程学习资料\\大四下学期\\毕业设计\\TestCases\\SheetJS\\excels\\tmp\\服务器端文件.xlsx";
        ExcelUtils.convertStringToExcel(string, filename);
        return "OK";
    }
}
