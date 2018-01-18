package cn.chenny3.secondHand.controller;

import cn.chenny3.secondHand.bean.ViewObject;
import cn.chenny3.secondHand.model.User;
import cn.chenny3.secondHand.service.UserService;
import cn.chenny3.secondHand.util.SecondHandUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;


public class LoginController {
    @Autowired
    UserService userService;

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ResponseBody
    public ViewObject login(User user, HttpSession httpSession){
        if(user.getId() > 0||StringUtils.isEmpty(user.getPassword())){
            return ViewObject.build(1,"请补全参数在执行操作");
        }

        User dbUser = userService.selectUser(user.getId());
        if(dbUser==null){
            return ViewObject.build(1,"登录的用户不合法");
        }
        //通过盐值处理 验证密码
        if(SecondHandUtil.MD5(user.getPassword()+dbUser.getSalt()).equals(dbUser.getPassword())){
            dbUser.setSalt("");
            dbUser.setPassword("");
            httpSession.setAttribute("user",dbUser);
            return ViewObject.build(0,"登录成功,jesessionId:"+httpSession.getId());
        }else{
            return ViewObject.build(1,"登录失败");
        }
    }

    @RequestMapping(value = "/logout",method = RequestMethod.GET)
    @ResponseBody
    public ViewObject login(HttpSession httpSession){
        User user =null;
        if(httpSession!=null){
            user = (User) httpSession.getAttribute("user");
            if(user!=null) {
                httpSession.removeAttribute("user");
                return ViewObject.build(0,"注销成功");
            }
        }

        return ViewObject.build(1,"注销操作非法");
    }
}
