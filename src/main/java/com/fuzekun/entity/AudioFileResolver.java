package com.fuzekun.entity;

import com.fuzekun.entity.absClass.FileResolver;
import com.fuzekun.utils.FileUtils;
import com.fuzekun.utils.TranslateUtils;
import com.sun.jdi.ClassNotLoadedException;
import io.github.givimad.whisperjni.WhisperContext;
import io.github.givimad.whisperjni.WhisperFullParams;
import io.github.givimad.whisperjni.WhisperJNI;
import io.github.givimad.whisperjni.WhisperSamplingStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.validation.Valid;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Path;
import java.util.concurrent.*;

/**
 * @author: fuzekun
 * @date: 2024/4/30
 * @describe: 音频文件的处理，直接翻译就行了
 * 1. 抛出错误，日志不用记录，直到根节点在进行日志的记录
 * 2. 一般来说，只有在服务文件中会进行日志的记录
 */

// 由于是线程安全的，所以说可以直接使用单例的
@Slf4j
@Component
public class AudioFileResolver extends FileResolver<File> {
    @Value("audio.save.path")
    private static final String SAVE_PATH = "d:\\data\\speak\\audio";
    @Value("${translate.model.path}")
    private static final String MODEL_FILE_PATH = "d:\\data\\models\\ggml-tiny.bin";
    @Value("${translate.language}")
    private static final String LANGUAGE = "en";
    @Value("${audio.save.suffix}")
    private static final String SUFFIX = ".wav";
    @Value("{translate.ans.suffix}")
    private static final String ANS_SUFFIX = ".txt";
    @Value("{ANS_FILE_PATH}")
    private static final String ANS_FILE_PATH = "d:\\data\\speak\\ans";
    @Value("{TMP_FILE_PATH}")
    private static final String TMP_FIME_PATH = "d:\\data\\speak\\tmp";

    private static final int CHANGE_SUCCESS_CODE = 0;

    // 交给spring，不用static自己写了，更加优雅。
    @PostConstruct
    public void init() {
        if (!FileUtils.mkDir(SAVE_PATH) || !FileUtils.mkDir(ANS_FILE_PATH) || !FileUtils.mkDir(TMP_FIME_PATH)) {
            log.error("Audio路径无法创建，请检查系统（win）是否正确!");
        }
    }

    // 首先加载类的时候就创建文件，缓存下来，模型文件还是挺大，这个缓存在Whisper类中已经有了
    private static Path testModelPath = Path.of(MODEL_FILE_PATH);
    private static File modelFile = testModelPath.toFile();
    private volatile static ThreadPoolExecutor poolExecutor;
    // 其实如果是单例的，完全用不用自己手动写双锁检测，但是可以尝试练手
    private void initThreadPool() {
        if (poolExecutor == null) {
            synchronized (this) {
                if (poolExecutor == null) {
                    poolExecutor = new ThreadPoolExecutor(10, 10, 250, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(10));
                }
            }
        }
    }

    @Override
    public String save(MultipartFile f) throws IOException {
        // 1. todo: 将文件保存在本地，生成uuid
        String fileName = SAVE_PATH + "\\" + (int)(Math.random() * 100) + SUFFIX;
        f.transferTo(new File(fileName));
        // 2. todo: 文件表，加一个外键，用以表示用户的id
        // 3. todo: 找到用户的所有文件可以通过 id进行筛选
        return fileName;
    }

    @Override
    public File resolve(File f) throws IOException {
        try {
            return this.resolveAsyn(f).get();
        } catch (InterruptedException e) {
            throw new RuntimeException("解析异常" + e.getMessage());
        } catch (ExecutionException e) {
            throw new RuntimeException("解析异常" + e.getMessage());
        }
    }
    /**
     * 异步文件解析：模型下载和文件预处理同步进行
     * */
    @Override
    public Future<File> resolveAsyn(File f) throws IOException, InterruptedException, ExecutionException {
        String tmpFile = TMP_FIME_PATH + "\\" + (int)(Math.random() * 100) + SUFFIX;
        String ansFile = ANS_FILE_PATH + "\\" + (int)(Math.random() * 100) + ANS_SUFFIX;
        Future<File>ans =  translate(f, tmpFile, ansFile);
        return ans;
    }


    /**
     * 需要临时生成一个文件，所以需要给定临时文件的名称
     * 使用UUID不用考虑临时文件的重复问题
     *
     * @return 是否翻译成功，成功保存到ans文件，失败删除tmp和ansFile；
     *
     * 1. 提升性能的手段: 缓存了模型，采用了双锁检测；使用多线程进行写入，从而提升响应速度
     *
     *
     * */
    public Future<File> translate(File sourceFile, String tmpFile, String ansFile) throws IOException, InterruptedException, ExecutionException {
        // 0. 检验有效性
        if(!modelFile.exists() || !modelFile.isFile()) {
            throw new RuntimeException("Missing model file: " + testModelPath.toAbsolutePath());
        }
        if (!sourceFile.exists()) {
            throw new RuntimeException("源文件缺失!");
        }

        // 1. 转换成64位的文件，然后在进行重新采样
        Future<float[]>task = changeAsyn(sourceFile, tmpFile);
        log.debug("--------1. {}开始文件转换-------", sourceFile.getName());

        // 2. 通过JNI调用C++库文件,从而实现调用本地的C++接口，使用了单例模式加载文件，所以不用担心性能问题了
        WhisperJNI.LoadOptions loadOptions = new WhisperJNI.LoadOptions();
        loadOptions.logger = System.out::println;
        WhisperJNI.loadLibrary(loadOptions);
        WhisperJNI whisper = new WhisperJNI();
        WhisperJNI.setLibraryLogger(loadOptions.logger);
        log.debug("-------- 2. Whisper类加载完成 ------");

        // 3. 进行翻译
        var ctx = whisper.init(testModelPath);
        if (ctx == null) throw new RuntimeException("初始化模型失败!");
        var params = new WhisperFullParams(WhisperSamplingStrategy.GREEDY, LANGUAGE);
        float[] samples = task.get();

        int result = whisper.full(ctx, params, samples, samples.length);
        if(result != 0) {
            throw new RuntimeException("Transcription failed with code " + result);
        }
        log.debug("-------- 3. 翻译完成 -----------");
        // 4. 异步写入结果，如果服务端支持推送功能，可以提升效率，否则还是串行执行
        return writeTranslateAnsToFileAsyn(whisper, ctx, ansFile);
    }
    /**异步文件处理*/
    private Future<float[]>changeAsyn(File sourceFile, String tmpFilePath)  {
        // 1. 初始化线程池
        initThreadPool();
        // 2. 进行文件转化任务的执行
        return poolExecutor.submit(()->{
            if (TranslateUtils.change16Bit(sourceFile, tmpFilePath) != CHANGE_SUCCESS_CODE) {
                throw new RuntimeException("转换异常，无法处理!");
            }
            Path samplePath = Path.of(tmpFilePath);
            if (!samplePath.toFile().exists())
                throw new RuntimeException("服务器处理异常!");
            float[] samples = readJFKFileSamples(samplePath);
            // 处理完成之后，删除临时文件
            samplePath.toFile().delete();
            return samples;
        });
    }
    /**异步写入文件*/
    private Future<File> writeTranslateAnsToFileAsyn(WhisperJNI whisper, WhisperContext ctx, String filePath) throws IOException{
        // 1. 初始化线程池
        initThreadPool();
        // 2. 使用线程池将结果异步写入文件
        return poolExecutor.submit(() -> {
            log.debug("-------- 4. 开始写入文件 -----------");
            File file = new File(filePath);
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            int numSegments = whisper.fullNSegments(ctx);
            for (int i = 0; i < numSegments; i++) {
                long startTime = whisper.fullGetSegmentTimestamp0(ctx, i);
                long endTime = whisper.fullGetSegmentTimestamp1(ctx, i);
                String timeStrap = getTime(startTime) + "-" + getTime(endTime);
//                System.out.print(timeStrap + " ");
                String text = whisper.fullGetSegmentText(ctx, i);
//                System.out.println(text);
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
            log.debug("------------ 文件写如完成 --------------");
            return file;
        });
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
