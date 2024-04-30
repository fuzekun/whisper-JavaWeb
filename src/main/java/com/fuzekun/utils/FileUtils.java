package com.fuzekun.utils;

/**
 * @author: fuzekun
 * @date: 2024/4/30
 * @describe:
 */
public class FileUtils {
    public static String getSuffix(String fileName) {
        int pos = fileName.indexOf(".");
        if (pos == -1) throw new IllegalArgumentException("需要包含文件后缀!");
        return fileName.substring(pos + 1);
    }
}
