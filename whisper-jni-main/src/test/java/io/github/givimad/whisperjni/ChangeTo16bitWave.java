package io.github.givimad.whisperjni;

import java.io.IOException;
import java.io.*;
import java.util.Scanner;

/**
 * @author: Zekun Fu
 * @date: 2024/4/20 18:14
 * @Description:
 */

public class ChangeTo16bitWave {

    public static void change(String sourceFile, String endFile) throws IOException {
        File inputFile = new File(sourceFile);
        File outFile = new File(endFile);
        if (!inputFile.exists()) {
            throw new IOException("输入文件不存在!");
        }
        if (outFile.exists()) {
            Scanner sc = new Scanner(System.in);
            System.out.print("临时文件" + endFile + "已经存在，已覆盖?[y:n]:");
//            String s = sc.nextLine();
            outFile.delete();
//            if (s.equals("n")) {
//                return ;
//            }
        }
        try {
            System.out.println("转化中...");
            // 构建FFmpeg命令
            String command = "ffmpeg -i " + sourceFile + " -ar 16000 -ac 1 -f wav " + endFile;

            // 执行命令
            Process process = Runtime.getRuntime().exec(command);

            // 等待命令执行完成
            process.waitFor();

            // 检查执行结果
            int exitValue = process.exitValue();
            if (exitValue == 0) {
                System.out.println("文件转成bit16位临时文件成功！");
            } else {
                System.out.println("文件转成bit16位临时文件失败,错误码：" + exitValue);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // 输入文件路径和输出文件路径
        String inputFilePath = "d:\\data\\speak\\test2.wav"; // 替换为你的输入文件路径
        String outputFilePath = "d:\\data\\speak\\output2.wav"; // 替换为你想要的输出文件路径

        // 调用转换方法
        try {
            change(inputFilePath, outputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
