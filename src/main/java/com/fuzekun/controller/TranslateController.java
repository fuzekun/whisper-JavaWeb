package com.fuzekun.controller;

import com.fuzekun.common.ResponseResult;
import com.fuzekun.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Zekun Fu
 * @date: 2024/4/30 23:08
 * @Description:
 */
@RestController
public class TranslateController {

    @Autowired
    FileService fileService;

    @RequestMapping(value = "/translate", method = RequestMethod.POST)
    public ResponseResult translateWithLanguage(@Nullable @RequestParam("language")String language, @RequestParam("sourceFile")String sourceFile) {
        return fileService.resolve(sourceFile);
    }

}
