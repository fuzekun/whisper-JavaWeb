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
public interface FileService {
    ResponseResult upload(MultipartFile file);
    ResponseResult resolve(String fileName);

}
