package cn.chenny3.secondHand.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.UUID;

public class SecondHandUtil {
    private static final Logger logger = LoggerFactory.getLogger(SecondHandUtil.class);
    private static ObjectMapper objectMapper=new ObjectMapper();

    /**
     * MD5
     * @param key
     * @return
     */
    public static String MD5(String key) {
        char hexDigits[] = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };
        try {
            byte[] btInput = key.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            logger.error("生成MD5失败", e);
            return null;
        }
    }

    /**
     * Object 转 JSON
     * @param object
     * @return
     * @throws JsonProcessingException
     */
    public static String getJsonString(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    /**
     * JSON 转 Object
     * @param json
     * @param clazz
     * @param <T>
     * @return
     * @throws IOException
     */
    public static<T> T getObjectFromJson(String json,Class<T> clazz) throws IOException {
        return objectMapper.readValue(json,clazz);
    }

    /**
     * 随机生成指定位数的随机码
     * @param length
     * @return
     */
    public static String generateSpecifiedLengthCode(int length){
        if(length<0||length>32) throw new IllegalArgumentException("length的有效范围为1至32位");
        return UUID.randomUUID().toString().replace("-","").substring(0,length);
    }
}
