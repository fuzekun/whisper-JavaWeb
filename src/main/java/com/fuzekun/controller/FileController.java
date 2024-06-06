package com.fuzekun.controller;

import com.fuzekun.common.ResponseResult;
import com.fuzekun.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


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

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public ResponseEntity<Resource> download(@RequestParam("filename") String fileName) {
        log.info("文件下载请求中...");
        ResponseEntity<Resource> ans =  fileService.download(fileName);
        log.info("文件下载完成...");
        return ans;
    }
}
