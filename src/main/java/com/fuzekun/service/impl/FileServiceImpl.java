package com.fuzekun.service.impl;

import com.fuzekun.common.ResponseResult;
import com.fuzekun.entity.AudioFileResolver;
import com.fuzekun.entity.TxtFileResolver;
import com.fuzekun.entity.absClass.FileResolver;
import com.fuzekun.service.FileService;
import com.fuzekun.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author: fuzekun
 * @date: 2024/4/29
 * @describe:
 */
@Slf4j
@Service
public class FileServiceImpl implements FileService {
    // 缓存工具类,构建之后就不会增加项目，所以是不变的
    private static final Map<String, FileResolver> resolverMap = new HashMap<>();
    @Autowired
    private TxtFileResolver<String> txtFileResolver;

    @Autowired
    private AudioFileResolver audioFileResolver;

    @PostConstruct
    private void init() {
        // 1. 构造的时候，自动封装下不同文件的处理类
        String[] fileKind = {"txt", "mp3", "wav", "xls", "xlsx"};
        FileResolver[] fileResolverClass = {txtFileResolver, audioFileResolver, audioFileResolver};
        // 2. 根据处理类进行封装，不要根据类型封装，有的类型不存在的
        for (int i = 0; i < fileResolverClass.length; i++) {
            resolverMap.put(fileKind[i], fileResolverClass[i]);
        }
    }

    @Override
    public ResponseResult upload(MultipartFile file) {
        // 0. 参数检验
        if (file == null) {
            return ResponseResult.error("文件为空，请选择需要上传的文件").build();
        }
        // 1. 选择保存器
        String type = FileUtils.getSuffix(file.getOriginalFilename());
        if (!resolverMap.containsKey(type)) {
            log.error("{}类型的文件无法保存!", type);
            return ResponseResult.error("该文件解析功能尚未开发!").build();
        }
        if (StringUtils.isEmpty(type)) return ResponseResult.error("文件名称为空，请检查上传的文件!").build();
        FileResolver resolver = resolverMap.get(type);

        // 2. 保存文件
        String fileName;
        try {
            fileName = resolver.save(file);
        } catch (IOException e) {
            return ResponseResult.error("文件保存失败：{}" + e.getMessage()).build();
        }
        log.info("{}文件保存为:{}", file.getOriginalFilename(), fileName);
        ResponseResult<String>responseResult = ResponseResult.ok("文件保存成功!").build();
        Map<String, String>jsonMap = new HashMap<>();
        jsonMap.put("fileName", fileName);
        responseResult.setData(jsonMap.toString());
        return responseResult;
    }

    @Override
    public ResponseResult resolve(String fileName) {
        // 0. 进行判断
        String type = FileUtils.getSuffix(fileName);
        if (!resolverMap.containsKey(type)) {
            log.error("{}类型的文件无法解析!", type);
            return ResponseResult.error("该文件解析功能尚未开发!").build();
        }
        File file = new File(fileName);
        if (!file.exists()) {
            log.error("文件不存在，没有上传文件，或者上传文件保存出错");
            return ResponseResult.error("请先上传文件!").build();
        }
        // 1. 根据具体文件类型，判断使用什么文件进行解析，使用工厂模式，将类型，映射到具体的解析类型
        FileResolver resolver = resolverMap.get(type);
        log.info("正在解析{}类型的文件，对应的解析器为:{}", type, resolver.getClass().getName());
        // 2. 进行实际上的解析
        try {
            resolver.resolve(file);
        } catch (IOException e) {
            log.error("文件转换失败:{}", e.getMessage());
        }
        log.info("{}语音文件翻译成功!", file.getName());
        return ResponseResult.ok("语音文件翻译成功").build();
    }

}
