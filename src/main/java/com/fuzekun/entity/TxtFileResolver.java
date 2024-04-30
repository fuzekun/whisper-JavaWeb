package com.fuzekun.entity;

import com.fuzekun.entity.absClass.FileResolver;
import com.fuzekun.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author: fuzekun
 * @date: 2024/4/30
 * @describe: txt文件解析器
 */
@Slf4j
@Component
public class TxtFileResolver<T> extends FileResolver<T> {

    @Value("${txt.save.path}")
    private static final String SAVE_PATH = "d:\\datat\\txt";
    static {
        File path = new File(SAVE_PATH);
        if (!path.exists()) {
            path.mkdir();
        }
    }
    @Override
    public String save(MultipartFile f) throws IOException {
        // 1. todo: 将文件保存在本地，生成uuid
        String fileName =(int)(Math.random() * 100) + ".txt";
        // 2. todo: 文件表，加一个外键，用以表示用户的id
        // 3. todo: 找到用户的所有文件可以通过 id进行筛选
        return FileUtils.saveFile(f, SAVE_PATH + "/" + fileName);
    }


    @Override
    public T resolve(File f) throws IOException {
        throw new RuntimeException("功能尚未开发，敬请期待!");
    }

    @Override
    public Future<T> resolveAsyn(File f) throws IOException, InterruptedException, ExecutionException {
        throw new RuntimeException("功能尚未开发，敬请期待!");
    }
}
