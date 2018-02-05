package cn.chenny3.secondHand.controller;

import cn.chenny3.secondHand.common.bean.dto.EasyResult;
import cn.chenny3.secondHand.common.utils.SecondHandUtil;
import cn.chenny3.secondHand.model.LoginRecord;
import cn.chenny3.secondHand.model.User;
import cn.chenny3.secondHand.service.LoginRecordService;
import cn.chenny3.secondHand.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.UUID;

@Controller
public class SSOController extends BaseController{
    @Autowired
    UserService userService;
    @Autowired
    LoginRecordService loginRecordService;
    @Value("${user.avatar.default}")
    private String defaultAvatar;

    @RequestMapping(value = "register",method = RequestMethod.POST)
    @ResponseBody
    public EasyResult register(User user){
        try {
            user.setHeadUrl(defaultAvatar);
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
    public EasyResult login(User user, HttpSession httpSession, HttpServletRequest request){
        if(user.getId() <= 0||StringUtils.isEmpty(user.getPassword())){
            return new EasyResult(1,"请补全参数在执行操作");
        }
        User dbUser = userService.selectUser(user.getId());
        if(dbUser==null){
            return new EasyResult(1,"登录的用户不合法");
        }
        //通过盐值处理 验证密码
        if(SecondHandUtil.MD5(user.getPassword()+dbUser.getSalt()).equals(dbUser.getPassword())){
            //清除掉安全数据
            dbUser.setSalt("");
            dbUser.setPassword("");
            //保存会话信息至session
            httpSession.setAttribute("user",dbUser);
            //添加登陆记录到数据库
            //todo 获取ip地址功能需补全，现在不能实现客户端代理访问时获取其真实ip
            LoginRecord loginRecord = new LoginRecord().setUserId(dbUser.getId()).setIp(request.getRemoteAddr());
            //0:0:0:0:0:0:0:1
            if(loginRecord.getIp().equals("0:0:0:0:0:0:0:1")){
                loginRecord.setIp("127.0.0.1");
            }
            loginRecordService.addLoginRecord(loginRecord);

            return new EasyResult(0,"登录成功,jesessionId:"+httpSession.getId());
        }else{
            return new EasyResult(1,"登录失败");
        }
    }

    @RequestMapping(value = "/logout",method = RequestMethod.GET)
    @ResponseBody
    public EasyResult logout(HttpSession httpSession){
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
