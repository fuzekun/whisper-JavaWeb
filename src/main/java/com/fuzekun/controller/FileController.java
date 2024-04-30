package com.fuzekun.controller;

import com.fuzekun.common.ResponseResult;

import com.fuzekun.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author: fuzekun
 * @date: 2024/4/29
 * @describe: 文件上传和下载controller
 */
@RestController
@RequestMapping("file")
public class FileController {

    @Autowired
    private FileService fileService;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseResult upload(@RequestParam("file") MultipartFile file) {

        return fileService.upload(file);
    }

}
