package cn.chenny3.secondHand.controller.web;

import cn.chenny3.secondHand.bean.ViewObject;
import cn.chenny3.secondHand.controller.BaseController;
import cn.chenny3.secondHand.model.LoginRecord;
import cn.chenny3.secondHand.model.User;
import cn.chenny3.secondHand.service.LoginRecordService;
import cn.chenny3.secondHand.service.UserService;
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
public class MemberController extends BaseController{
    @Autowired
    private LoginRecordService loginRecordService;
    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public String view(Model model){
        ViewObject viewObject = new ViewObject();
        viewObject.put("categories",categoryService.selectCategoriesByParentId(0));
        model.addAttribute("vo",viewObject);
        return "member/member";
    }
    @RequestMapping(value = "user",method = RequestMethod.GET)
    public String userInfo(Model model,HttpSession httpSession){
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            return "login";
        }else{
            int userId=user.getId();
            List<LoginRecord> loginRecords = loginRecordService.selectLastLoginRecord(userId);
            ViewObject viewObject = new ViewObject();
            viewObject.put("loginRecords",loginRecords);
            viewObject.put("user",user);
            model.addAttribute("vo",viewObject);
            return "member/user";
        }

    }

    @RequestMapping("ip")
    @ResponseBody
    public String getIp(HttpServletRequest request){
        return request.getRemoteAddr();
    }

    @RequestMapping(value = "address_list")
    public String addressList(){
        return "member/address_list";
    }
}
