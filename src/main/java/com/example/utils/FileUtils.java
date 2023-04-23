package com.example.utils;

import java.io.File;

/**
 * @author Lex
 */
public class FileUtils {
    private static boolean isOnWindowsSystem() {
        String osName = System.getProperty("os.name").toLowerCase();
        return osName.contains("win");
    }

    public static String getFilePath() {
        String filePath;
        String currentDir = new File("").getAbsolutePath();
        if (isOnWindowsSystem()) {
            filePath = new File(currentDir).getParent() + File.separator + "excels" + File.separator + "serverExcel.xlsx";
        } else {
            // 如果服务器不在Windows Server上运行,就在Linux上运行,不考虑Mac
            // 由于文件结构差异,Linux系统上运行的是jar文件而不是工程文件夹下的某个类,因此获取到的路径层次不一样
            filePath = new File(currentDir) + File.separator + "excels" + File.separator + "serverExcel.xlsx";
        }
        return filePath;
    }
}
