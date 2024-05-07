package io.github.givimad.whisperjni;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuzekun.common.ResponseResult;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

/**
 * @author: Zekun Fu
 * @date: 2024/4/30 23:29
 * @Description:
 */
public class PostUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static boolean validResponse(String response) throws IOException {
        ResponseResult responseResult =  objectMapper.readValue(response.toString(), ResponseResult.class);
        if (responseResult.getStatusCode() == ResponseResult.ok) {
            System.out.println("post请求执行成功: " + responseResult.getMessage());
            System.out.println("返回结果为:" + responseResult.getData());
            return true;
        }
        else {
            System.out.println("post请求执行失败: " + responseResult.getMessage());
            return false;
        }
    }
    /**
     * 使用名字作为参数仍旧可以上传
     * */
    public static String sendBase64File(String url, String filePath) throws IOException {
        File file = new File(filePath);
        return sendBase64File(url, new File(filePath));
    }
    public static String sendBase64File(String url, File file) throws IOException {
        byte[] fileContent = Files.readAllBytes(file.toPath());
        String base64FileContent = Base64.getEncoder().encodeToString(fileContent);

        Map<String, String> postData = new HashMap<>();
        postData.put("file", base64FileContent);
        return sendPostWithParam(url, postData);
    }
    /**
     * 使用文件名称作为请求，仍旧可以上传
     * */
    public static String sendMultipartPostRequest(String url, String fileName) throws IOException {
        return sendMultipartPostRequest(url, new File(fileName));
    }
    /**
     *
     * 发送文件返回post的响应body字符串
     * */
    public static String sendMultipartPostRequest(String url, File file) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addBinaryBody("file", file, ContentType.DEFAULT_BINARY, file.getName());

        HttpEntity multipart = builder.build();
        httpPost.setEntity(multipart);

        HttpResponse response = httpClient.execute(httpPost);
        String responseBody = EntityUtils.toString(response.getEntity());
        return responseBody;
    }
    /**
     * 发送文件返回请求体，可以根据请求体进行判断是否返回成功
     * */
    public static String sendPostWithParam(String uri, Map<String, String> mp) throws IOException{
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(uri);

        // 设置请求参数
        List<NameValuePair> params = new ArrayList<>();
        for (Map.Entry<String, String>entry : mp.entrySet()) {
            params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }

        httpPost.setEntity(new UrlEncodedFormEntity(params));

        // 发送请求并获取响应
        HttpResponse response = httpClient.execute(httpPost);
        String responseBody = EntityUtils.toString(response.getEntity());
        return responseBody;
    }
}
