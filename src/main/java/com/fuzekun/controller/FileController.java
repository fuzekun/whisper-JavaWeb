package com.fuzekun.controller;

import com.fuzekun.common.ResponseResult;
import com.fuzekun.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * @author: Zekun Fu
 * @date: 2024/4/29 22:30
 * @Description: 实现文件的上传和下载
 */
@Slf4j
@RequestMapping("file")
@RestController
public class FileController {

    @Autowired
    private FileService fileService;
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseResult upload(MultipartFile file) {
        log.debug("上传文件请求中...");
        return fileService.upload(file);
    }
}
