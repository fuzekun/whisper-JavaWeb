package io.github.givimad.whisperjni;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuzekun.dto.ResponseResult;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author: fuzekun
 * @date: 2024/4/30
 * @describe:
 */
public class TestUploadFile {
    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            URL url = new URL("http://localhost:8080/file/upload"); // 上传文件的URL
            File file = new File("d:\\data\\password.txt"); // 要上传的文件路径

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            String boundary = "*****";
            String lineEnd = "\r\n";
            String twoHyphens = "--";

            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            OutputStream outputStream = connection.getOutputStream();

            outputStream.write((twoHyphens + boundary + lineEnd).getBytes());
            outputStream.write(("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getName() + "\"" + lineEnd).getBytes());
            outputStream.write(("Content-Type: image/jpeg" + lineEnd).getBytes());
            outputStream.write(lineEnd.getBytes());

            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            fileInputStream.close();

            outputStream.write(lineEnd.getBytes());
            outputStream.write((twoHyphens + boundary + twoHyphens + lineEnd).getBytes());

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder responseContentBuffer = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                responseContentBuffer.append(line);
            }
            ResponseResult responseResult = objectMapper.readValue(responseContentBuffer.toString(), ResponseResult.class);
            if (responseResult.getStatus()) {
                System.out.println("文件上传成功!");
            } else {
                System.out.println("文件上传失败:" + responseResult.getMessage());
            }

            outputStream.close();
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
