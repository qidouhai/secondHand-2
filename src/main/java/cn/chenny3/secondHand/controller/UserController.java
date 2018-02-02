package cn.chenny3.secondHand.controller;

import cn.chenny3.secondHand.common.bean.UserHolder;
import cn.chenny3.secondHand.common.bean.dto.EasyResult;
import cn.chenny3.secondHand.common.utils.SecondHandUtil;
import cn.chenny3.secondHand.model.User;
import cn.chenny3.secondHand.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {
    @Autowired
    UserService userService;
    @Autowired
    private UserHolder userHolder;

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
            return new EasyResult(1,"查询失败");
        }
    }

    @RequestMapping(value = "password",method = RequestMethod.PUT)
    @ResponseBody
    public EasyResult updatePasword(@RequestParam("originalPassword") String originalPassword,
                                    @RequestParam("newPassword") String newPassword) {
        try{
            if(StringUtils.isBlank(originalPassword)&&originalPassword.length()<6){
                return new EasyResult(1,"输入的原始密码不足6位");
            }
            if (StringUtils.isBlank(newPassword)&&newPassword.length()<6) {
                return new EasyResult(1,"输入的新密码不足6位");
            }
            int userId=userHolder.get().getId();
            //从数据库中查出用户的密码和盐
            User user = userService.selectUser(userId);
            String salt=user.getSalt();
            String dbEncryptPassword=user.getPassword();
            //对输入的密码参数进行加密处理
            String handledOriginalPassword=SecondHandUtil.MD5(originalPassword+salt);
            String handledNewPassword=SecondHandUtil.MD5(newPassword+salt);
            //原始密码验证
            if(dbEncryptPassword.equals(handledOriginalPassword)){
                //新密码和原始密码一致
                if(dbEncryptPassword.equals(handledNewPassword)){
                    return new EasyResult(1,"新密码和原始密码不能相等");
                }
                userService.updatePassword(userId,newPassword);
                return new EasyResult(0,"密码修改成功。下次登录请使用新密码！");
            }
            return new EasyResult(1,"输入的原始密码不正确");

        }catch (Exception e){
            logger.error(e.getMessage());
            return new EasyResult(1,"密码修改失败");
        }
    }

    @RequestMapping(value = "phone",method = RequestMethod.PUT)
    @ResponseBody
    public EasyResult updatePhone(@RequestParam("phone") String phone) {
        try{
            if (StringUtils.isBlank(phone)&&phone.length()!=11) {
                return new EasyResult(1,"输入的手机号不足11位");
            }
            userService.updatePhone(userHolder.get().getId(),phone);
            //更新会话中的用户信息
            userHolder.get().setPhone(phone);
            return new EasyResult(0,"手机号修改成功。");
        }catch (Exception e){
            logger.error(e.getMessage());
            return new EasyResult(1,"手机号密码修改失败");
        }
    }

    @RequestMapping(value = "email",method = RequestMethod.PUT)
    @ResponseBody
    public EasyResult updateEmail(@RequestParam("email") String email) {
        try{
            if (StringUtils.isBlank(email)&&!email.matches("[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+")) {
                return new EasyResult(1,"请输入正确格式的邮箱");
            }
            userService.updateEmail(userHolder.get().getId(),email);
            //更新会话中的用户信息
            userHolder.get().setEmail(email);
            return new EasyResult(0,"手机号修改成功。");
        }catch (Exception e){
            logger.error(e.getMessage());
            return new EasyResult(1,"手机号密码修改失败");
        }
    }
}
