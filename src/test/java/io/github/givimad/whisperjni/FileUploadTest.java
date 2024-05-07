package io.github.givimad.whisperjni;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuzekun.common.ResponseResult;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;

/**
 * @author: Zekun Fu
 * @date: 2024/4/29 22:55
 * @Description:
 */
public class FileUploadTest {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static String filePath = "d:\\data\\password.txt";
    private static String uploadUrl = "http://localhost:8081/file/upload";



    @Test
    public void sendMultipartPostRequest() throws IOException {
        assert PostUtils.validResponse(PostUtils.sendMultipartPostRequest(uploadUrl, filePath));
    }

    @Test
    public void sendBase64FileTest() throws IOException {
        assert PostUtils.validResponse(PostUtils.sendBase64File(uploadUrl, filePath));
    }
}
