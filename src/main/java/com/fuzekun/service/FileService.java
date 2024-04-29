package com.fuzekun.service;

import com.fuzekun.common.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * @author: Zekun Fu
 * @date: 2024/4/29 22:38
 * @Description:
 */
@Slf4j
@Service
public class FileService {

    public ResponseResult upload(MultipartFile file) {
        if (file == null) {
            log.debug("文件为空!");
            return ResponseResult.error("文件为空，请检查文件是否存在!").build();
        }
        try {
            log.debug("读取文件!");
            InputStream fin = file.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(fin));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            log.error("文件读取失败" + e.getMessage());
            return ResponseResult.error("文件读取失败，请重新上传文件！").build();
        }
        log.debug("文件上传成功!");
        return ResponseResult.ok("文件上传成功!").build();

    }
}
