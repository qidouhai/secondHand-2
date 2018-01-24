package cn.chenny3.secondHand.controller;

import cn.chenny3.secondHand.commons.result.EasyResult;
import cn.chenny3.secondHand.commons.enums.EntityType;
import cn.chenny3.secondHand.model.Comment;
import cn.chenny3.secondHand.service.CommentService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import java.util.*;

@Controller
@RequestMapping("/comment")
public class CommentController extends BaseController {
    @Autowired
    private CommentService commentService;

    private ObjectMapper objectMapper=new ObjectMapper();
    {
        //objectMapper过滤空值属性
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public String addComment(Comment comment, BindingResult bindingResult) throws JsonProcessingException {
        EasyResult easyResult=null;
        try {
            if (bindingResult.hasErrors()) {
                easyResult=new EasyResult(1, objectErrorsToString(bindingResult));
                return objectMapper.writeValueAsString(easyResult);
            }
            commentService.addComment(comment);
            int count = commentService.selectCount(comment.getEntityId(), comment.getEntityType(), comment.getParentId());
            if(count==1){//留言新增第一条回复时，修改isParent属性值
                Comment parent = new Comment();
                parent.setId(comment.getParentId());
                parent.setIsParent(2);
                parent.setUpdated(new Date());
                commentService.updateComment(parent);
            }
            easyResult=new EasyResult(0, comment);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            easyResult=new EasyResult(1, "评论添加失败");
        }
        return objectMapper.writeValueAsString(easyResult);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public String getComments(@RequestParam int goodsId) throws JsonProcessingException {
        EasyResult easyResult=null;
        try {
            if (goodsId <= 0) {
                easyResult= new EasyResult(1, "参数违法");
                return objectMapper.writeValueAsString(easyResult);
            }
            easyResult = new EasyResult();
            easyResult.setCode(0);
            //查询指定商品的一级评论
            List<Comment> oneLevelComments = commentService.selectComments(goodsId, EntityType.GOODS, 0);
            ArrayList<Map<String, Object>> list = new ArrayList<>();
            for(Comment oneLevelComment:oneLevelComments){
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("main",oneLevelComment);
                if(oneLevelComment.getIsParent()==1){
                    map.put("reply",Collections.EMPTY_LIST);
                }else{
                    map.put("reply",commentService.selectComments(goodsId,EntityType.GOODS,oneLevelComment.getId()));
                }
                list.add(map);
            }
            easyResult.setMsg(list);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            easyResult=new EasyResult(1, "操作错误。请报告管理员！\n" + e.getMessage());
        }
        return objectMapper.writeValueAsString(easyResult);
    }
}
