package io.github.givimad.whisperjni;

import lombok.extern.slf4j.Slf4j;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import java.util.Map;

/**
 * @author: Zekun Fu
 * @date: 2024/4/30 21:37
 * @Description:
 */
@Slf4j
public class TranslateTest {


    @Test
    public void testTransportWaveFile() {
        File wavFile = new File("d:\\data\\speak\\output.wav");
        try {
            FileUpload.sendMultipartPostRequest("http://localhost:8081/file/upload", wavFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTranslate() throws IOException{
        String uri = "http://localhost:8081/translate";
        Map<String, String>mp = new HashMap<>();
        String[] keys = {"sourceFile"};
        String[] vals = {"d:\\data\\audio\\18.wav"};
        for (int i = 0; i < keys.length; i++) {
            mp.put(keys[i], vals[i]);
        }
        assert PostUtils.validResponse(PostUtils.sendPostWithParam(uri, mp));
    }
}
