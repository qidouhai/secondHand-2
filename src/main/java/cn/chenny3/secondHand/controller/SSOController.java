package cn.chenny3.secondHand.controller;

import cn.chenny3.secondHand.bean.EasyResult;
import cn.chenny3.secondHand.bean.ViewObject;
import cn.chenny3.secondHand.model.Base;
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
import java.util.UUID;

@Controller
public class SSOController extends BaseController{
    @Autowired
    UserService userService;

    @RequestMapping(value = "register",method = RequestMethod.POST)
    @ResponseBody
    public EasyResult register(User user){
        try {

            //todo:跳转到信息完善页面
            user.setHeadUrl("img/avatar/c8eb62d426f146ed91e737c14bd1fb90.png");
            String randomStr= UUID.randomUUID().toString().replace("-","");
            user.setStuId(randomStr.substring(0,10));
            user.setDeptName("计算机学院");
            user.setSubjectName("计算机科学与技术");
            user.setQq(user.getStuId());
            user.setWechat(user.getStuId());
            user.setAlipay(user.getStuId());

            userService.addUser(user);

            return new EasyResult(0,"注册成功");
        }catch (Exception e){
            logger.error(e.getMessage());
            return new EasyResult(1,"注册失败");
        }
    }

    @RequestMapping(value = {"login","register"},method = RequestMethod.GET)
    public String view(){
        return "login";
    }

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ResponseBody
    public EasyResult login(User user, HttpSession httpSession){
        if(user.getId() <= 0||StringUtils.isEmpty(user.getPassword())){
            return new EasyResult(1,"请补全参数在执行操作");
        }

        User dbUser = userService.selectUser(user.getId());
        if(dbUser==null){
            return new EasyResult(1,"登录的用户不合法");
        }
        //通过盐值处理 验证密码
        if(SecondHandUtil.MD5(user.getPassword()+dbUser.getSalt()).equals(dbUser.getPassword())){
            dbUser.setSalt("");
            dbUser.setPassword("");
            httpSession.setAttribute("user",dbUser);
            return new EasyResult(0,"登录成功,jesessionId:"+httpSession.getId());
        }else{
            return new EasyResult(1,"登录失败");
        }
    }

    @RequestMapping(value = "/logout",method = RequestMethod.GET)
    @ResponseBody
    public EasyResult login(HttpSession httpSession){
        User user =null;
        if(httpSession!=null){
            user = (User) httpSession.getAttribute("user");
            if(user!=null) {
                httpSession.removeAttribute("user");
                return new EasyResult(0,"注销成功");
            }
        }

        return new EasyResult(1,"注销操作非法");
    }
}
