package cn.chenny3.secondHand.controller;

import cn.chenny3.secondHand.common.bean.dto.EasyResult;
import cn.chenny3.secondHand.model.Content;
import cn.chenny3.secondHand.service.ContentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ContentController extends BaseController {
    @Autowired
    private ContentService contentService;

    private ObjectMapper objectMapper=new ObjectMapper();

    @RequestMapping(value = "/content/list/{entityType}/{curPage}/{pageSize}", method = RequestMethod.GET)
    @ResponseBody
    public EasyResult selectContent(@PathVariable("entityType") int entityType,
                                    @PathVariable(value = "curPage", required = false) int curPage,
                                    @PathVariable(value = "pageSize", required = false) int pageSize) {
        EasyResult easyResult = null;
        if (entityType <= 0 && curPage < 0 && pageSize < 0) {
            return easyResult = new EasyResult(1,"请求参数非法");
        }

        if (curPage == 0) {
            curPage = 1;
        }
        if (pageSize == 0) {
            pageSize = 5;
        }

        List<Content> contentList = contentService.selectContents(entityType, curPage,pageSize);

        try {
            easyResult = new EasyResult(0,objectMapper.writeValueAsString(contentList));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return easyResult;
    }

    @RequestMapping(value = "/content/{id}", method = RequestMethod.GET)
    @ResponseBody
    public EasyResult selectContent(@PathVariable(value = "id") int id) {
        EasyResult easyResult = null;
        if (id <= 0 ) {
            return easyResult=new EasyResult(1,"请求参数非法");
        }

        Content content = contentService.selectContent(id);
        try {
            easyResult=new EasyResult(0,objectMapper.writeValueAsString(content));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return easyResult;
    }

}
