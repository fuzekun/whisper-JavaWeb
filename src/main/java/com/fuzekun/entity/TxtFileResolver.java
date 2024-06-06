package com.fuzekun.entity;

import com.fuzekun.annotation.Monitor;
import com.fuzekun.entity.absClass.FileResolver;
import com.fuzekun.utils.FileUtils;
import com.fuzekun.utils.SnowflakeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.swing.plaf.PanelUI;
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
    private String SAVE_PATH = "d:\\data\\speak\\ans";
    @PostConstruct
    public void init() {
        File path = new File(SAVE_PATH);
        if (!path.exists()) {
            log.info("txt保存文件夹创建成功：{}", path.mkdir());
        }
    }

    @Override
    public ResponseEntity<Resource> downLoad(String fileName) {
        // 读取文件
        Resource resource = new FileSystemResource(SAVE_PATH + "/" + fileName);

        // 设置下载响应头
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        // 构建响应实体
        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }

    @Monitor
    @Override
    public String save(MultipartFile f) throws IOException {
        // 1. todo: 将文件保存在本地，生成uuid
        String fileName = SnowflakeUtil.genId() + ".txt";
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
