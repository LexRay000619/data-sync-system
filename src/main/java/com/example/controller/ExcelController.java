package com.example.controller;

import com.example.service.WebSocketServer;
import com.example.utils.ExcelUtils;
import com.example.utils.FileUtils;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author Lex
 */
@CrossOrigin(origins = {"*"})
@RestController
public class ExcelController {
    static Log log = LogFactory.getLog(ExcelController.class);

    @PostMapping("/saveExcelToServer")
    public String saveExcelToServer(@RequestBody String jsonString) {
        String filePath = FileUtils.getFilePath();
        ExcelUtils.convertStringToExcel(jsonString, filePath);
        for (WebSocketServer item : WebSocketServer.webSocketSet) {
            try {
                item.sendMessage("全服广播: " + "有窗口更新excel数据,此更新将同步至所有开启文件的窗口！");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "新的Excel数据已更新至服务器文件中！";
    }

    @GetMapping("/getJsonStringFromServer")
    public String getJsonStringFromServer() throws Exception {
        log.info("Excel文件数据已经通过JSON字符串形式发送至客户端！");
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
