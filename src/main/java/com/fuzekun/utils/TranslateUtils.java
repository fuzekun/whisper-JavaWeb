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
    public static int change16Bit(File inputFile, String endFile) throws IOException, InterruptedException {
        File outFile = new File(endFile);
        if (!inputFile.exists()) {
            throw new IOException("输入文件不存在!");
        }
        if (outFile.exists()) {
            Scanner sc = new Scanner(System.in);
            log.info("文件{}已经存在，文件被覆盖", outFile);
            outFile.delete();
        }

        log.info("转化中...源文件：{}， 临时文件：{}", inputFile.getAbsoluteFile(), endFile);
        // 构建FFmpeg命令，注意应该是绝对路径
        String command = "ffmpeg -i " + inputFile.getAbsoluteFile() + " -ar 16000 -ac 1 -f wav " + endFile;

        // 执行命令
        Process process = Runtime.getRuntime().exec(command);

        // 等待命令执行完成
        process.waitFor();

        // 检查执行结果
        int code =  process.exitValue();
        log.info("文件转化完成...{}", code);


        return code;
    }

    public static void main(String[] args) {
        // 输入文件路径和输出文件路径
        String inputFilePath = "d:\\data\\audio\\18.wav"; // 替换为你的输入文件路径
        String outputFilePath = "d:\\data\\speak\\tmp\\3.wav"; // 替换为你想要的输出文件路径

        // 调用转换方法
        try {
            int code = change16Bit(new File(inputFilePath), outputFilePath);
            System.out.println(code);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
