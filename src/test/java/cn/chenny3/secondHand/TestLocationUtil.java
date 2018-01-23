package cn.chenny3.secondHand;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestLocationUtil {
    private final OkHttpClient client = new OkHttpClient();
    @Test
    public void test() throws IOException {
        RequestBody formBody = new FormBody.Builder()
                .add("ak", "kWEveobogqxAR2IkycfjIkMXgkUyuM21")
                .add("ip","223.152.229.25")
                .add("coor","gcj02")
                .build();
        Request request = new Request.Builder()
                .url("http://api.map.baidu.com/location/ip")
                .post(formBody)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        String content=response.body().string();
        System.out.println(content);
        ObjectMapper objectMapper=new ObjectMapper();
        JsonNode tree = objectMapper.readTree(content);
        int status=tree.get("status").asInt();
        if(status==0){
            String address = tree.get("content").get("address").asText();
            System.out.println(address);
        }
    }
}
