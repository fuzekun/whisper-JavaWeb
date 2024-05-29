package com.fuzekun.controller;

import com.fuzekun.common.ResponseResult;
import com.fuzekun.entity.AudioFileResolver;
import com.fuzekun.service.FileService;
import com.fuzekun.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author: Zekun Fu
 * @date: 2024/4/30 23:08
 * @Description:
 */
@Slf4j
@RestController
public class TranslateController {

    @Autowired
    FileService fileService;

    @RequestMapping(value = "/translate", method = RequestMethod.GET)
    public ResponseResult translateWithLanguage(@Nullable @RequestParam("language")String language, @RequestParam("sourceFile")String sourceFile) {
        return fileService.resolve(sourceFile);
    }


    @PostMapping(value = "/translate")
    public ResponseResult translateWithLanguage(@Nullable @RequestParam("language")String language, @RequestBody MultipartFile file) {
        // 接收文件,随机生成文件名称
        try {
            String fileName = AudioFileResolver.SAVE_PATH + "\\" + FileUtils.getRandomFileName() + AudioFileResolver.SUFFIX;
            FileUtils.saveFile(file, fileName);
            // 进行翻译
            return fileService.resolve(fileName);
        } catch (IOException e) {
            log.error("文件保存失败\n" + e);
            return ResponseResult.error("文件翻译失败" + e).build();
        }
    }



}
