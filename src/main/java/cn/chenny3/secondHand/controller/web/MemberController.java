package cn.chenny3.secondHand.controller.web;

import cn.chenny3.secondHand.commons.bean.UserHolder;
import cn.chenny3.secondHand.commons.vo.ViewObject;
import cn.chenny3.secondHand.controller.BaseController;
import cn.chenny3.secondHand.model.LoginRecord;
import cn.chenny3.secondHand.model.User;
import cn.chenny3.secondHand.model.UserAuthenticate;
import cn.chenny3.secondHand.service.AddressService;
import cn.chenny3.secondHand.service.LoginRecordService;
import cn.chenny3.secondHand.service.UserAuthenticateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("member")
public class MemberController extends BaseController {
    @Autowired
    private LoginRecordService loginRecordService;
    @Autowired
    private UserAuthenticateService authenticateService;
    @Autowired
    UserHolder userHolder;

    @RequestMapping(method = RequestMethod.GET)
    public String view() {
        return "member/member";
    }

    @RequestMapping(value = "user", method = RequestMethod.GET)
    public String userInfo(Model model) {
        User user = userHolder.get();
        //获取当前用户上次和本次的登陆记录
        List<LoginRecord> loginRecords = loginRecordService.selectLastLoginRecord(user.getId());
        //获取当前用户的身份认证信息
        UserAuthenticate userAuthenticate = authenticateService.selectAuthenticate(user.getAuthenticateId());
        ViewObject viewObject = new ViewObject().
                put("loginRecords", loginRecords).
                put("userAuthenticateInfo", userAuthenticate);
        model.addAttribute("vo", viewObject);
        return "member/user";

    }



    @RequestMapping("ip")
    @ResponseBody
    public String getIp(HttpServletRequest request) {
        return request.getRemoteAddr();
    }


}
