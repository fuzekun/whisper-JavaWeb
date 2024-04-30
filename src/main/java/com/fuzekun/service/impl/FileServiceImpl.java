package com.fuzekun.service.impl;

import com.fuzekun.dto.ResponseResult;
import com.fuzekun.entity.absClass.FileResolver;
import com.fuzekun.service.FileService;
import com.fuzekun.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: fuzekun
 * @date: 2024/4/29
 * @describe:
 */
@Slf4j
@Service
public class FileServiceImpl implements FileService {
    private static final Map<String, String> resolverMap = new HashMap<>();
    static {
        // {mp3, wav, txt, xls, xlsx}
        String[] fileKind = {"txt", "mp3", "wav", "txt", "xls", "xlsx"};
        String[] fileResolverClass = {"com.fuzekun.entity.TxtFileResolver"};
        for (int i = 0; i < fileResolverClass.length; i++) {
            resolverMap.put(fileKind[i], fileResolverClass[i]);
        }
    }

    @Override
    public ResponseResult upload(MultipartFile file) {
        // 0. 参数检验
        if (file == null) {
            return ResponseResult.Builder.buildError("文件为空，请选择需要上传的文件");
        }

        String fileName = file.getOriginalFilename();
        String type = FileUtils.getSuffix(fileName);

        if (!resolverMap.containsKey(type)) {
            log.error("{}类型的文件无法解析!", type);
            return ResponseResult.Builder.buildError("该文件解析功能尚未开发!");
        }
        // 1. 根据具体文件类型，判断使用什么文件进行解析，使用工厂模式，将类型，映射到具体的解析类型
        String resolverClassName = resolverMap.get(type);
        log.info("正在解析{}类型的文件，对应的解析器为:{}", type, resolverClassName);
        // 3.保存文件
        try {
            FileResolver fileResolver = (FileResolver) Class.forName(resolverClassName).newInstance();
            fileResolver.resolve(file);
        } catch (ClassNotFoundException e) {
            log.error("文件上传失败：\n" + e);
            return ResponseResult.Builder.buildError("文件上传失败");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return ResponseResult.Builder.buildOk("文件解析成功");
    }
}
