package cn.chenny3.secondHand.controller;

import cn.chenny3.secondHand.bean.ViewObject;
import cn.chenny3.secondHand.model.User;
import cn.chenny3.secondHand.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {
    @Autowired
    UserService userService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ViewObject addUser(@Valid User user, BindingResult bindingResult) {
        ViewObject vo = new ViewObject();
        try {
            if (bindingResult.hasErrors()) {
                vo.put("code", 1);
                vo.put("value", objectErrorsToString(bindingResult));
                return vo;
            }

            userService.addUser(user);
            vo.put("code", 0);
            vo.put("value", "保存成功");
        } catch (Exception e) {
            logger.error(e.getMessage());
            vo.put("code", 1);
            vo.put("value", "保存错误");
        }
        return vo;
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ViewObject deleteUser(@PathVariable("id") int userId) {
        ViewObject vo = new ViewObject();
        try{
            if(userId <= 0){
                vo.put("code", 1);
                vo.put("value", "请求参数非法");
                return vo;
            }
            userService.deleteUser(userId);
            vo.put("code", 0);
            vo.put("value", "删除成功");
        }catch (Exception e){
            logger.error(e.getMessage());
            vo.put("code", 1);
            vo.put("value", "删除失败");
        }
        return vo;
    }

    @RequestMapping(value = "{id}",method = RequestMethod.GET)
    @ResponseBody
    public ViewObject selectUser(@PathVariable("id") int userId) {
        ViewObject vo = new ViewObject();
        try{
            if(userId <= 0){
                return vo.build(1,"请求参数非法");
            }
            User user = userService.selectUser(userId);
            return vo.build(0,user);
        }catch (Exception e){
            logger.error(e.getMessage());
            return vo.build(1,"查询失败");
        }
    }
}
