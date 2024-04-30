package com.fuzekun.entity;

import com.fuzekun.entity.absClass.FileResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: fuzekun
 * @date: 2024/4/30
 * @describe: txt文件解析器
 */
@Slf4j
public class TxtFileResolver extends FileResolver {

    // 保存路径
    private static final File SAVE_PATH = new File("d:\\datat\\txt");
    static {
        if (!SAVE_PATH.exists()) {
            SAVE_PATH.mkdir();
        }
    }
    @Override
    public boolean resolve(MultipartFile f) {
        if (f == null) throw new NullPointerException("文件为空！");

        List<String> fileContent = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(f.getInputStream()))){
            String line;
            while ((line = reader.readLine()) != null) {
                fileContent.add(line);
            }
        } catch (IOException e) {
            log.error("文件保存失败{}", e.getLocalizedMessage());
            return false;
        }
        String fileName = saveFile(fileContent);
        log.info("文件{}保存成功，对应的文件名为:{}", f.getName(), fileName);
        return true;
    }
    private String saveFile(List<String> fileContent) {
        // 1. 将文件保存在本地，生成uuid
        String fileName = (int)(Math.random() * 100) + ".txt";
        // 2. 文件表，加一个外键，用以表示用户的id
        // 3. 找到用户的所有文件可以通过 id进行筛选

        //tmp:这里仅仅进行测试，不进行保存了
        for (String line : fileContent) {
            System.out.println(line);
        }
        return fileName;
    }
}
