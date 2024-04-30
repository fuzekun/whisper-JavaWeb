package com.fuzekun.entity.absClass;

import com.fuzekun.utils.TranslateUtils;
import io.github.givimad.whisperjni.WhisperContext;
import io.github.givimad.whisperjni.WhisperFullParams;
import io.github.givimad.whisperjni.WhisperJNI;
import io.github.givimad.whisperjni.WhisperSamplingStrategy;
import lombok.extern.slf4j.Slf4j;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Path;

/**
 * @author: fuzekun
 * @date: 2024/4/30
 * @describe: 音频文件的处理，直接翻译就行了
 * 1. 抛出错误，日志不用记录，直到根节点在进行日志的记录
 * 2. 一般来说，只有在服务文件中会进行日志的记录
 */
public class AudioFileResolver {
    private static final String modelFilePahth = "d:\\data\\models\\ggml-tiny.bin";
    // 中文是zh，英文是en
    private static final String language = "en";
    private static Path testModelPath = Path.of(modelFilePahth);
    /**
     * 需要临时生成一个文件，所以需要给定临时文件的名称
     * 使用UUID不用考虑临时文件的重复问题
     *
     * @return 是否翻译成功，成功保存到ans文件，失败删除tmp和ansFile；
     * */
    public void translate(String sourceFile, String tmpFile, String ansFile) throws IOException, UnsupportedAudioFileException, InterruptedException {
        // 0. 检验有效性
        File modelFile = testModelPath.toFile();
        if(!modelFile.exists() || !modelFile.isFile()) {
            throw new RuntimeException("Missing model file: " + testModelPath.toAbsolutePath());
        }
        if (!new File(sourceFile).exists()) {
            throw new RuntimeException("源文件缺失!");
        }

        // 1. 转换成64位的文件，然后在进行重新采样
        if (TranslateUtils.change16Bit(sourceFile, tmpFile) != 0) {
            throw new RuntimeException("转换异常，无法处理");
        }
        Path samplePath = Path.of(tmpFile);
        float[] samples = readJFKFileSamples(samplePath);

        // 2. 通过JNI调用C++库文件,从而实现调用本地的C++接口，创建Whisper独享
        var loadOptions = new WhisperJNI.LoadOptions();
        loadOptions.logger = System.out::println;
        WhisperJNI.loadLibrary(loadOptions);
        WhisperJNI whisper = new WhisperJNI();
        WhisperJNI.setLibraryLogger(loadOptions.logger);


        // 3. 进行翻译
        var ctx = whisper.init(testModelPath);
        if (ctx == null) throw new RuntimeException("初始化模型失败!");
        var params = new WhisperFullParams(WhisperSamplingStrategy.GREEDY, language);
        int result = whisper.full(ctx, params, samples, samples.length);
        if(result != 0) {
            throw new RuntimeException("Transcription failed with code " + result);
        }
        // 4. 写入结果
        writeTranslateAnsToFile(whisper, ctx, ansFile);
    }

    private void writeTranslateAnsToFile(WhisperJNI whisper, WhisperContext ctx, String file) throws IOException{
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        int numSegments = whisper.fullNSegments(ctx);
        for (int i = 0; i < numSegments; i++) {
            long startTime = whisper.fullGetSegmentTimestamp0(ctx,i);
            long endTime = whisper.fullGetSegmentTimestamp1(ctx,i);
            String timeStrap = getTime(startTime) + "-" + getTime(endTime);
            System.out.print(timeStrap + " ");
            String text = whisper.fullGetSegmentText(ctx, i);
            System.out.println(text);
            writer.write(timeStrap + " ");
            writer.write(text + "\n");
        }
        writer.write("\n全部内容如下：\n");
        writer.write("---------------------------------------------------------\n");
        // 全写
        for (int i = 0; i < numSegments; i++) {
            String text = whisper.fullGetSegmentText(ctx, i);
            writer.write(text + "\n");
        }
        writer.write("-------------------------------------------------------------");
        writer.flush();
        writer.close();
    }

    private String getTime(long startTime) {
        // 默认为s的100倍数，所以先转化成s
        long res = startTime / 100;
//        long res = startTime;
        long second = res % 60; res = res / 60;
        long min = res % 60; res = res / 60;
        long hour = res % 1000;
        String sec = second < 10 ? "0" + second : "" + second;
        String m = min < 10 ? "0" + min : "" + min;
        String h = hour < 10 ? "0" + hour : "" + hour;
        return String.format("%s:%s:%s", h, m, sec);
    }
    private float[] readJFKFileSamples(Path samplePath) throws UnsupportedAudioFileException, IOException {
        // sample is a 16 bit int 16000hz little endian wav file
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(samplePath.toFile());
        // read all the available data to a little endian capture buffer
        ByteBuffer captureBuffer = ByteBuffer.allocate(audioInputStream.available());
        captureBuffer.order(ByteOrder.LITTLE_ENDIAN);
        int read = audioInputStream.read(captureBuffer.array());
        if (read == -1) {
            throw new IOException("Empty file");
        }
        // obtain the 16 int audio samples, short type in java
        var shortBuffer = captureBuffer.asShortBuffer();
        // transform the samples to f32 samples
        float[] samples = new float[captureBuffer.capacity() / 2];
        var i = 0;
        while (shortBuffer.hasRemaining()) {
            samples[i++] = Float.max(-1f, Float.min(((float) shortBuffer.get()) / (float) Short.MAX_VALUE, 1f));
        }
        return samples;
    }
}
