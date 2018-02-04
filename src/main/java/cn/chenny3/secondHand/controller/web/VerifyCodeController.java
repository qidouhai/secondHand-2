package cn.chenny3.secondHand.controller.web;

import cn.chenny3.secondHand.common.bean.UserHolder;
import cn.chenny3.secondHand.common.bean.dto.EasyResult;
import cn.chenny3.secondHand.common.utils.*;
import cn.chenny3.secondHand.controller.BaseController;
import cn.chenny3.secondHand.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("verifyCode")
public class VerifyCodeController extends BaseController{
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    MailSenderUtils mailSenderUtils;
    @Autowired
    UserHolder userHolder;
    @Autowired
    AliyunMessageUtil aliyunMessageUtil;

    @RequestMapping("updatePhone")
    @ResponseBody
    public EasyResult getUpdatePhoneVerifyCode(@RequestParam("phone") String phone){
        try{
            //手机格式验证
            if (StringUtils.isBlank(phone)&&!phone.matches("1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}")) {
                return new EasyResult(1,"请输入正确格式的11为手机号");
            }
            //当前用户是否登陆
            User user = userHolder.get();
            if(user==null){
                return new EasyResult(1,"请在登陆情况下尝试获取验证码");
            }
            //生成4为随机验证码
            String verifyCode= SecondHandUtil.generateSpecifiedLengthCode(4);
            //组合手机和验证码
            String joinStr=phone+"#"+verifyCode;
            //获取保存至redis的键
            String key=RedisKeyUtils.getUpdatePhoneAccountKey(user.getId());
            //redis保存有效期有15分钟
            redisUtils.set(key,joinStr,60*15L);
            //发送短信
            aliyunMessageUtil.send(phone,verifyCode);
            return new EasyResult(0,"验证码已发送至手机");
        }catch (Exception e){
            logger.error(e.getMessage());
            return new EasyResult(0,"验证码获取失败");
        }
    }

    @RequestMapping("updateEmail")
    @ResponseBody
    public EasyResult getUpdateEmailVerifyCode(@RequestParam("email") String email){
       try{
           //邮箱格式验证
           if (StringUtils.isBlank(email)&&!email.matches("[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+")) {
               return new EasyResult(1,"请输入正确格式的邮箱");
           }
           //当前用户是否登陆
           User user = userHolder.get();
           if(user==null){
               return new EasyResult(1,"请在登陆情况下尝试获取验证码");
           }
           //生成4为随机验证码
           String verifyCode= SecondHandUtil.generateSpecifiedLengthCode(4);
           //组合邮箱和验证码
           String joinStr=email+"#"+verifyCode;
           //获取保存至redis的键
           String key=RedisKeyUtils.getUpdateEmailAccountKey(user.getId());
           //redis保存有效期有15分钟
           redisUtils.set(key,joinStr,60*15L);
           //发送邮件
           mailSenderUtils.sendVerifyCodeMail(email,verifyCode);

           return new EasyResult(0,"验证码已发送至邮箱");
       }catch (Exception e){
            logger.error(e.getMessage());
           return new EasyResult(0,"验证码获取失败");
       }
    }
}
