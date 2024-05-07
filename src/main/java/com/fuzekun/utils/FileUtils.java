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
    // 将文件保存到filePath中
    public static boolean saveFile(File sourceFile, String filePath) throws IOException {
        // 首先创建文件夹
        if (filePath.contains("\\"))  {
            String[]paths = filePath.split("\\\\");
            if(!mkDirHepler(paths, paths.length - 1, new StringBuilder()))
                return false;
        }
        // 创建文件
        File targetFile = new File(filePath);
        if (!targetFile.exists()) {
            if (targetFile.createNewFile())
                return false;
        }
        // 移动文件
        return sourceFile.renameTo(new File(filePath));
    }
    /** 保存mutipartFile文件，保存的路径为fileName */
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

    /** 获取文件的后缀 */
    public static String getSuffix(String fileName) {
        if (fileName == null) throw new NullPointerException("文件名为空!");
        int pos = fileName.indexOf(".");
        if (pos == -1) throw new IllegalArgumentException("需要包含文件后缀!");
        return fileName.substring(pos + 1);
    }

    /**递归 or 循环的方式创建文件夹*/
    public static boolean mkDir(String path) {
        String[]paths = path.split("\\\\");
        if (paths.length == 0) paths = new String[]{path};
//        return mkDirHepler(paths, 0, new StringBuilder());
        return mkDirHepler(paths);
    }
    private static boolean mkDirHepler(String[] paths) {
        StringBuilder curPath = new StringBuilder();
        for (String path : paths) {
            curPath.append("\\").append(path);
            File file = new File(curPath.toString());
            if (!file.exists()) {
                if (!file.mkdir())
                    return false;
            }
        }
        return true;
    }
    private static boolean mkDirHepler(String[] paths, int cur, StringBuilder curPath) {
        if (cur == paths.length) {
            return true;
        }
        curPath.append("\\").append(paths[cur]);
        File pathFile = new File(curPath.toString());
        if (!pathFile.exists()) {
            if (!pathFile.mkdir())
                return false;
        }
        return mkDirHepler(paths, cur + 1, curPath);
    }
}
