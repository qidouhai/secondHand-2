package cn.chenny3.secondHand.common.utils;

import cn.chenny3.secondHand.model.UserAuthenticate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;

/**
 * 湖南理工学院信息门户网站 工具类
 */
@Component
public class HnistPortalUtil {
    @Autowired
    OkHttpUtils okHttpUtils;

    private ObjectMapper objectMapper=new ObjectMapper();

    /**
     * 根据用户提供的信息门户JessionId 访问网站获取学号
     *
     * @param jsessionId
     * @return 学号id
     */
    private String getId(String jsessionId) throws IOException {
        String stuId = null;
        HashMap<String, String> headers = new HashMap<>();
        headers.put("cookie", "JSESSIONID=" + jsessionId);
        Response response = okHttpUtils.syncGetOnlyNet("http://portal.hnist.cn/web/guest/242", headers, Collections.emptyMap());
        if (response.isSuccessful()) {
            String content = response.body().string();
            response.close();
            Document document = Jsoup.parse(content);
            stuId = document.select("input[name=id]").get(0).val();
        }
        return stuId;
    }

    /**
     * 获取信息门户中对应用户的身份信息
     * @param jsessionId
     * @return
     */
    public UserAuthenticate getAuthenticateInfo(String jsessionId) throws Exception {
        UserAuthenticate userAuthenticate=null;
        //获取学生id
        String stuId = getId(jsessionId);
        //设置请求头
        HashMap<String,String> headers=new HashMap<>();
        headers.put("Content-Type","application/x-www-form-urlencoded");
        //设置请求体
        HashMap<String,String> params=new HashMap<>();
        params.put("id","24");
        params.put("treeId","1");
        params.put("currentPage","1");
        params.put("screenName",stuId);
        params.put("options","%5B%5D");
        params.put("option","");
        params.put("isDrillingType","true");
        params.put("needQuery","false");
        params.put("needCache","true");
        //发起请求
        Response response = okHttpUtils.syncPost("http://portal.hnist.cn/portal_complex_bg/ViewRelationAction.do", headers, params);
        if(response.isSuccessful()){
            String content = response.body().string();
            response.close();
            String tableData = objectMapper.readTree(content).get("tableData").asText();
            ArrayNode arrayNode = (ArrayNode) objectMapper.readTree(tableData);
            //解析响应体，并封装数据
            String stuName=arrayNode.get(1).get("data").get(3).get("value").asText();
            String sex=arrayNode.get(2).get("data").get(1).get("value").asText();
            String schoolName=arrayNode.get(12).get("data").get(3).get("value").asText();
            String deptName=arrayNode.get(13).get("data").get(1).get("value").asText();
            String subjectName=arrayNode.get(13).get("data").get(5).get("value").asText();//⊕
            if(subjectName.trim().charAt(0)=='⊕'){
                subjectName=subjectName.substring(1);
            }
            int registerYear=arrayNode.get(14).get("data").get(1).get("value").asInt();
            String className=arrayNode.get(14).get("data").get(5).get("value").asText();

            userAuthenticate = new UserAuthenticate(stuId,stuName,sex,schoolName,deptName,subjectName,className,registerYear);

        }
        return userAuthenticate;
    }
}
