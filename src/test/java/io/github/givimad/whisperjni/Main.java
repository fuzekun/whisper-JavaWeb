package io.github.givimad.whisperjni;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Path;

import static io.github.givimad.whisperjni.WhisperGrammar.assertValidGrammar;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author: Zekun Fu
 * @date: 2024/4/20 16:53
 * @Description:
 */


public class Main {

    private static final String modelFilePahth = "d:\\data\\models\\ggml-tiny.bin";
    // 中文是zh，英文是en
    private static final String language = "en";
    // 原文件和临时文件不能相同
    private static final String sourceFile = "d:\\data\\speak\\testEnglish.mp3";
    private static final String tmpFile = "d:\\data\\speak\\outputEnglish.wav";
    private static final String ansFilePath = "d:\\data\\speak\\ansFile.txt";
    private static Path testModelPath = Path.of(modelFilePahth);
    private static Path samplePath = Path.of(tmpFile);
//    private static Path sampleAssistantGrammar = Path.of("src/main/native/whisper/grammars/assistant.gbnf");
//    private static Path sampleChessGrammar = Path.of("src/main/native/whisper/grammars/chess.gbnf");
//    private static Path sampleColorsGrammar = Path.of("src/main/native/whisper/grammars/colors.gbnf");
    private static WhisperJNI whisper;
    private static BufferedWriter writer;
    static {

        try {
            File ansFile = new File(ansFilePath);
            if (!ansFile.exists()) {
                ansFile.createNewFile();
            }
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(ansFile)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args) throws Exception {
        translate();
    }
    public static void translate() throws Exception {
        ChangeTo16bitWave.change(sourceFile, tmpFile);
        System.out.println(System.getProperty("user.dir"));
        var modelFile = testModelPath.toFile();
        var sampleFile = samplePath.toFile();
        if(!modelFile.exists() || !modelFile.isFile()) {
            throw new RuntimeException("Missing model file: " + testModelPath.toAbsolutePath());
        }
        if(!sampleFile.exists() || !sampleFile.isFile()) {
            throw new RuntimeException("Missing sample file");
        }
        var loadOptions = new WhisperJNI.LoadOptions();
        loadOptions.logger = System.out::println;
        // 首先通过JNI调用C++库文件,从而实现调用本地的C++接口
        WhisperJNI.loadLibrary(loadOptions);
        // 日志不用记录了
        WhisperJNI.setLibraryLogger(null);
        whisper = new WhisperJNI();

        float[] samples = readJFKFileSamples();
        try (var ctx = whisper.init(testModelPath)) {
            assertNotNull(ctx);
            var params = new WhisperFullParams(WhisperSamplingStrategy.GREEDY, language);
            int result = whisper.full(ctx, params, samples, samples.length);
            if(result != 0) {
                throw new RuntimeException("Transcription failed with code " + result);
            }
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
//            assertEquals(0, startTime);
//            assertEquals(1050, endTime);
//            assertEquals(" And so my fellow Americans ask not what your country can do for you, ask what you can do for your country.", text);
        }
    }

    private static String getTime(long startTime) {
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
//    public void validateGrammar() throws ParseException, IOException {
//        assertValidGrammar(sampleAssistantGrammar);
//        assertValidGrammar(sampleColorsGrammar);
//        assertValidGrammar(sampleChessGrammar);
//    }
    private static float[] readJFKFileSamples() throws UnsupportedAudioFileException, IOException {
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
