package cn.chenny3.secondHand.controller;

import cn.chenny3.secondHand.bean.EasyResult;
import cn.chenny3.secondHand.model.Comment;
import cn.chenny3.secondHand.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
@RequestMapping("/comment")
public class CommentController extends BaseController{
    @Autowired
    private CommentService commentService;

    @RequestMapping(method = RequestMethod.POST)
    public EasyResult addComment(@Valid Comment comment, BindingResult bindingResult){
        try{
            if(bindingResult.hasErrors()){
                return new EasyResult(1,objectErrorsToString(bindingResult));
            }
            commentService.addComment(comment);
            return new EasyResult(0,comment);
        }catch (Exception e){
            logger.error(e.getMessage());
            return new EasyResult(1,"评论添加失败");
        }
    }
}
