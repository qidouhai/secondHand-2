package cn.chenny3.secondHand.controller;

import cn.chenny3.secondHand.commons.result.EasyResult;
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
    public EasyResult addUser(@Valid User user, BindingResult bindingResult) {
        EasyResult result = new EasyResult();
        try {
            if (bindingResult.hasErrors()) {
                result.setCode( 1);
                result.setMsg(objectErrorsToString(bindingResult));
                return result;
            }

            userService.addUser(user);
            result.setCode(0);
            result.setMsg("保存成功");
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setCode(1);
            result.setMsg("保存错误");
        }
        return result;
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public EasyResult deleteUser(@PathVariable("id") int userId) {
        try{
            if(userId <= 0){
                return new EasyResult(1,"请求参数非法");
            }
            userService.deleteUser(userId);
            return new EasyResult(0,"删除成功");
        }catch (Exception e){
            logger.error(e.getMessage());
            return new EasyResult(1,"删除失败");
        }
    }

    @RequestMapping(value = "{id}",method = RequestMethod.GET)
    @ResponseBody
    public EasyResult selectUser(@PathVariable("id") int userId) {
        try{
            if(userId <= 0){
                return new EasyResult(1,"请求参数非法");
            }
            User user = userService.selectUser(userId);
            return new EasyResult(0,user);
        }catch (Exception e){
            logger.error(e.getMessage());
            return new EasyResult(0,"查询失败");
        }
    }
}
