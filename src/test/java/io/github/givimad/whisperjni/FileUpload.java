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

/**
 * @author: Zekun Fu
 * @date: 2024/4/29 22:55
 * @Description:
 */
public class FileUpload {

    private static String filePath = "d:/data/password.txt";
    private static String uploadUrl = "http://localhost:8081/file/upload";

    public static void main(String[] args) {

        try {
            File file = new File(filePath);
            sendMultipartPostRequest(uploadUrl, file);
//            sendBase64File();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void sendBase64File() throws IOException {
        File file = new File(filePath);
        byte[] fileContent = Files.readAllBytes(file.toPath());
        String base64FileContent = Base64.getEncoder().encodeToString(fileContent);

        Map<String, String> postData = new HashMap<>();
        postData.put("file", base64FileContent);

        sendPostRequest(uploadUrl, postData);
    }
    public static void sendMultipartPostRequest(String url, File file) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addBinaryBody("file", file, ContentType.DEFAULT_BINARY, file.getName());

        HttpEntity multipart = builder.build();
        httpPost.setEntity(multipart);

        HttpResponse response = httpClient.execute(httpPost);
        String responseBody = EntityUtils.toString(response.getEntity());
        PostUtils.validResponse(responseBody);
    }

    private static void sendPostRequest(String url, Map<String, String> postData) throws IOException {
        URL postUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        StringBuilder requestBody = new StringBuilder();
        for (Map.Entry<String, String> entry : postData.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            requestBody.append(key).append("=").append(value).append("&");
        }

        connection.getOutputStream().write(requestBody.toString().getBytes());
        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        }
        PostUtils.validResponse(response.toString());
        connection.disconnect();
    }

}
