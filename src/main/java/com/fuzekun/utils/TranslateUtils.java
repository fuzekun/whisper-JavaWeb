package com.fuzekun.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * @author: fuzekun
 * @date: 2024/4/30
 * @describe:
 */
@Slf4j
public class TranslateUtils {
    /**
     * 将音频文件，转化成16采样的音频文件
     */
    public static int change16Bit(String sourceFile, String endFile) throws IOException, InterruptedException {
        File inputFile = new File(sourceFile);
        File outFile = new File(endFile);
        if (!inputFile.exists()) {
            throw new IOException("输入文件不存在!");
        }
        if (outFile.exists()) {
            Scanner sc = new Scanner(System.in);
            log.debug("文件{}已经存在，文件被覆盖[y:n]:", outFile);
            outFile.delete();
        }

        log.debug("转化中...");
        // 构建FFmpeg命令
        String command = "ffmpeg -i " + sourceFile + " -ar 16000 -ac 1 -f wav " + endFile;

        // 执行命令
        Process process = Runtime.getRuntime().exec(command);

        // 等待命令执行完成
        process.waitFor();

        // 检查执行结果
        return process.exitValue();
    }

    public static void main(String[] args) {
        // 输入文件路径和输出文件路径
        String inputFilePath = "d:\\data\\speak\\test2.wav"; // 替换为你的输入文件路径
        String outputFilePath = "d:\\data\\speak\\output2.wav"; // 替换为你想要的输出文件路径

        // 调用转换方法
        try {
            change16Bit(inputFilePath, outputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
