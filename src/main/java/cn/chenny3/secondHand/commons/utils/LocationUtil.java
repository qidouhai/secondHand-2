package cn.chenny3.secondHand.commons.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;

@Component
public class LocationUtil {
    @Autowired
    private OkHttpEngine okHttpEngine;

    public String getRealLocation(String ip) throws IOException {
        String address = null;
        HashMap<String, String> params = new HashMap<>();
        params.put("ak", "kWEveobogqxAR2IkycfjIkMXgkUyuM21");
        params.put("ip", ip);
        params.put("coor", "gcj02");

        Response response = okHttpEngine.syncPost("http://api.map.baidu.com/location/ip", params);

        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        String content = response.body().string();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode tree = objectMapper.readTree(content);
        int status = tree.get("status").asInt();
        if (status == 0) {
            address = tree.get("content").get("address").asText();
        } else {
            //todo 需要改进
            address = "测试地址";
        }
        return address;
    }
}
