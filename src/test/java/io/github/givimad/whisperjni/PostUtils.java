package io.github.givimad.whisperjni;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuzekun.common.ResponseResult;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: Zekun Fu
 * @date: 2024/4/30 23:29
 * @Description:
 */
public class PostUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static void validResponse(String response) throws IOException {
        ResponseResult responseResult =  objectMapper.readValue(response.toString(), ResponseResult.class);
        if (responseResult.getStatusCode() == ResponseResult.ok) {
            System.out.println("post请求执行成功: " + responseResult.getMessage());
            System.out.println("返回结果为:" + responseResult.getData());
        }
        else {
            System.out.println("post请求执行失败: " + responseResult.getMessage());
        }
    }



    public static void sendPostWithParam(String uri, Map<String, String> mp) {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(uri);

        // 设置请求参数
        List<NameValuePair> params = new ArrayList<>();
        for (Map.Entry<String, String>entry : mp.entrySet()) {
            params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params));

            // 发送请求并获取响应
            HttpResponse response = httpClient.execute(httpPost);
            String responseBody = EntityUtils.toString(response.getEntity());
            PostUtils.validResponse(responseBody);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
