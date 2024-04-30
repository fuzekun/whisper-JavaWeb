package com.fuzekun.service;

import com.fuzekun.common.ResponseResult;
import java.io.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: fuzekun
 * @date: 2024/4/29
 * @describe:
 */
public interface FileService {

    public ResponseResult upload(MultipartFile file);

    public ResponseResult resolve(String fileName);

//    FileActionResponse uploadFile(byte[] bytes, String fileName);
//
//    FileActionResponse uploadFile(byte[] bytes);
//
//    FileActionResponse downloadFile(long fileId);
//
//    FileActionResponse deleteFile(long fileId);
//
//    void checkMd5(String chunk, String chunkSize, String guid, HttpServletResponse response);
//
//    void upload(MultipartFile file, Integer chunk, String guid) throws IOException;
//
//    FileActionResponse combineBlock(String guid, String fileName);
}
