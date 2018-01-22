package cn.chenny3.secondHand.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.IOException;

public class LocationUtil {
    private static final OkHttpClient client = new OkHttpClient();

    public static String getRealLocation(String ip) throws IOException {
        String address = null;
        RequestBody formBody = new FormBody.Builder()
                .add("ak", "kWEveobogqxAR2IkycfjIkMXgkUyuM21")
                .add("ip", ip)
                .add("coor", "gcj02")
                .build();
        Request request = new Request.Builder()
                .url("http://api.map.baidu.com/location/ip")
                .post(formBody)
                .build();

        Response response = null;

        response = client.newCall(request).execute();
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
