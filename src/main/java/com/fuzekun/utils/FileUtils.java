package com.fuzekun.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: fuzekun
 * @date: 2024/4/30
 * @describe:
 */
public class FileUtils {
    public static String saveFile(MultipartFile f, String fileName) throws IOException {
        // 0. 判断
        if (f == null) throw new NullPointerException("文件为空！");
        // 1. 读取文件内容
        List<String> fileContent = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(f.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            fileContent.add(line);
        }
        // 2. 保存文件
        //tmp:这里仅仅进行测试，不进行保存了. ok:完成了e
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        for (String l : fileContent) {
//            System.out.println(l);
            writer.write(l);
            writer.newLine();
        }
        writer.flush();
        writer.close();
        return fileName;
    }
    public static String getSuffix(String fileName) {
        if (fileName == null) throw new NullPointerException("文件名为空!");
        int pos = fileName.indexOf(".");
        if (pos == -1) throw new IllegalArgumentException("需要包含文件后缀!");
        return fileName.substring(pos + 1);
    }
}
