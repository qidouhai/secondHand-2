package cn.chenny3.secondHand.controller.manager;

import cn.chenny3.secondHand.Constants;
import cn.chenny3.secondHand.common.bean.dto.EasyResult;
import cn.chenny3.secondHand.common.bean.enums.RoleType;
import cn.chenny3.secondHand.common.bean.vo.ViewObject;
import cn.chenny3.secondHand.common.utils.SecondHandUtil;
import cn.chenny3.secondHand.model.Address;
import cn.chenny3.secondHand.model.LoginRecord;
import cn.chenny3.secondHand.model.User;
import cn.chenny3.secondHand.model.UserAuthenticate;
import cn.chenny3.secondHand.service.AddressService;
import cn.chenny3.secondHand.service.LoginRecordService;
import cn.chenny3.secondHand.service.UserAuthenticateService;
import cn.chenny3.secondHand.service.UserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/manager/member")
@Controller
public class MemberMgtController {
    @Autowired
    private UserService userService;
    @Autowired
    private LoginRecordService loginRecordService;
    @Autowired
    private UserAuthenticateService userAuthenticateService;
    @Autowired
    private AddressService addressService;

    /**
     *用户列表
     * @param isDel 是否删除
     * @param start 开始行数
     * @param offset 记录数
     * @return
     */
    @RequestMapping(value = "list")
    @ResponseBody
    public Map list(@RequestParam("isDel") int isDel, @RequestParam(name = "start") int start,@RequestParam(name = "length") int offset){
        Map<String,Object> userMap=null;
        List<Map<String,Object>> userMapList=new ArrayList<>();
        //查询用户数量
        int count = userService.selectUserListCount(isDel);
        if(count!=0){
            List<User> userList = userService.selectUserList(isDel,start,offset);
            //重新封装数据
            if (userList != null) {
                for (User user : userList) {
                    userMap=new HashMap<>();
                    userMap.put("id",user.getId());
                    userMap.put("name",user.getName());
                    userMap.put("sex",user.getUserAuthenticate().getSex());
                    userMap.put("phone",user.getPhone());
                    userMap.put("email",user.getEmail());
                    userMap.put("addressId",user.getAddressId());
                    userMap.put("authenticateId",user.getAuthenticateId());
                    Address address = user.getAddress();
                    userMap.put("address",String.format("%s院 %d栋 %d号", address.getArea(), address.getHostelId(), address.getHouseId()));
                    userMap.put("createTime", SecondHandUtil.date2String("yyyy-MM-dd HH:mm:ss",user.getCreated()));
                    userMap.put("status",user.getStatus());
                    userMapList.add(userMap);
                }
            }
        }
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("data",userMapList);
        resultMap.put("recordsTotal",count);
        resultMap.put("recordsFiltered",count);
        return resultMap;
    }

    @RequestMapping("/info/{userId}")
    public String userInfo(@PathVariable int userId, Model model){
        User user = userService.selectUser(userId);
        //获取当前用户上次和本次的登陆记录
        List<LoginRecord> loginRecords = loginRecordService.selectLastLoginRecord(userId);
        //获取当前用户的身份认证信息
        UserAuthenticate userAuthenticate = userAuthenticateService.selectAuthenticate(user.getAuthenticateId());
        //获取当前用户的地址信息
        Address address = addressService.select(user.getAddressId());

        ViewObject viewObject = new ViewObject().
                put("user",user).
                put("loginRecords", loginRecords).
                put("userAuthenticateInfo", userAuthenticate).
                put("address", address);
        model.addAttribute("vo", viewObject);
        return "/manager/member/member-show";
    }


    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ResponseBody
    public EasyResult login(User user, HttpSession httpSession, HttpServletRequest request){
        if(StringUtils.isEmpty(user.getName())||StringUtils.isEmpty(user.getPassword())){
            return new EasyResult(1,"请补全参数在执行操作");
        }
        User dbUser = userService.selectUser(user.getName());
        if(dbUser==null){
            return new EasyResult(1,"用户名不存在");
        }

        //通过盐值处理 验证密码
        if(SecondHandUtil.MD5(user.getPassword()+dbUser.getSalt()).equals(dbUser.getPassword())){
            if(!dbUser.getRole().equals(RoleType.Admin)){
                return new EasyResult(1,"非管理员用户不能登录");
            }
            //清除掉安全数据
            dbUser.setSalt("");
            dbUser.setPassword("");
            //保存会话信息至session
            httpSession.setAttribute("user",dbUser);
            //添加登陆记录到数据库
            LoginRecord loginRecord = new LoginRecord().setUserId(dbUser.getId()).setIp(request.getRemoteAddr());
            loginRecordService.addLoginRecord(loginRecord);

            return new EasyResult(0,"登录成功");
        }else{
            return new EasyResult(1,"密码错误");
        }
    }


    //用户管理-密码修改界面
    @RequestMapping(value = "/change-password/{id}",method = RequestMethod.GET)
    public String changePassword(@PathVariable int id,Model model){
        User user = userService.selectUser(id);
        model.addAttribute("vo",new ViewObject().put("user",user));
        return "/manager/member/change-password";
    }


    //用户管理-密码修改
    @RequestMapping(value = "/change-password",method = RequestMethod.POST)
    @ResponseBody
    public EasyResult changePassword(User user){
        try{
            if(user!=null){
                if(user.getId()>0&&!StringUtils.isEmpty(user.getPassword())){
                    userService.updatePassword(user.getId(),user.getPassword());
                    return new EasyResult(0,"密码修改成功");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new EasyResult(1,"密码修改失败");
    }



    @RequestMapping(value = "delete",method = RequestMethod.POST)
    @ResponseBody
    public EasyResult batchDelete(@RequestParam int ids[]){
        try{
            userService.batchUpdateStatus(ids, Constants.ENTITY_STATUS_DELETE);
            return new EasyResult(0,"删除用户成功");
        }catch (Exception e){
            return new EasyResult(1,"删除用户失败");
        }
    }

    @RequestMapping(value = "disable/{id}",method = RequestMethod.POST)
    @ResponseBody
    public EasyResult disabledUser(@PathVariable int id){
        try{
            userService.updateStatus(id, Constants.USER_STATUS_DISABLE);
            return new EasyResult(0,"禁用用户成功");
        }catch (Exception e){
            return new EasyResult(1,"禁用用户失败");
        }
    }


    @RequestMapping(value = "enable/{id}",method = RequestMethod.POST)
    @ResponseBody
    public EasyResult enableUser(@PathVariable int id){
        try{
            userService.updateStatus(id, Constants.USER_STATUS_ENABLE);
            return new EasyResult(0,"启用用户成功");
        }catch (Exception e){
            return new EasyResult(1,"启用用户失败");
        }
    }
}
