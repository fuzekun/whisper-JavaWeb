package com.fuzekun.entity.absClass;

import org.springframework.web.multipart.MultipartFile;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author: fuzekun
 * @date: 2024/4/30
 * @describe: 定义文件解析器
 * 所有文件的解析流程
 */
public abstract class FileResolver<T> {
    public abstract T resolve(File f) throws IOException;
    public abstract String save(MultipartFile f) throws IOException;
    public abstract Future<T> resolveAsyn(File f) throws IOException, InterruptedException, ExecutionException;

}
