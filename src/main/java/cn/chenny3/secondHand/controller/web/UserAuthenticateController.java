package cn.chenny3.secondHand.controller.web;

import cn.chenny3.secondHand.common.bean.UserHolder;
import cn.chenny3.secondHand.common.bean.dto.EasyResult;
import cn.chenny3.secondHand.common.utils.HnistPortalUtil;
import cn.chenny3.secondHand.common.utils.RedisKeyUtils;
import cn.chenny3.secondHand.common.utils.RedisUtils;
import cn.chenny3.secondHand.common.utils.SecondHandUtil;
import cn.chenny3.secondHand.controller.BaseController;
import cn.chenny3.secondHand.model.User;
import cn.chenny3.secondHand.model.UserAuthenticate;
import cn.chenny3.secondHand.service.UserAuthenticateService;
import cn.chenny3.secondHand.service.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserAuthenticateController extends BaseController{
    @Autowired
    private UserAuthenticateService userAuthenticateService;
    @Autowired
    private HnistPortalUtil hnistPortalUtil;
    @Autowired
    private UserService userService;
    @Autowired
    private UserHolder userHolder;
    @Autowired
    private RedisUtils redisUtils;

    /**
     * 认证信息获取
     * @param hnistJsessionId
     * @return
     */
    @RequestMapping(value = "member/authenticate",method = RequestMethod.POST)
    @ResponseBody
    public EasyResult authenticate(@RequestParam("jsessionId")String hnistJsessionId){
        try {
            User user= userHolder.get();
            if (user.getAuthenticateId()>0) {
                return new EasyResult(1,"用户已认证，无需操作");
            }
            //通过OKHTTP 获取用户身份信息
            UserAuthenticate authenticateInfo = hnistPortalUtil.getAuthenticateInfo(hnistJsessionId);
            //检测认证信息是否被其他用户所使用
            if(userAuthenticateService.selectAuthenticateByStuId(authenticateInfo.getStuId())!=null){
                return new EasyResult(1,"此认证信息已被其他用户认证，请重新认证");
            }
            //获取保存至redis的业务key
            String key= RedisKeyUtils.getAuthenticateInfoAssociateKey(user.getId());
            //授权信息保存至redis，有效期90秒
            String value=SecondHandUtil.getJsonString(authenticateInfo);
            redisUtils.set(key, value,90L);

            return new EasyResult(0,authenticateInfo);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new EasyResult(1,"认证信息获取失败,请重新按步骤获取jessionId");
        }
    }

    /**
     * 认证信息确认
     * @return
     */
    @RequestMapping(value = "member/authenticate/confirm",method = RequestMethod.POST)
    @ResponseBody
    public EasyResult confirmAuthenticate(){
        try {
            User user= userHolder.get();
            if (user.getAuthenticateId()>0) {
                return new EasyResult(1,"用户已认证，无需操作");
            }
            //从Redis中获取关联认证信息
            String key = RedisKeyUtils.getAuthenticateInfoAssociateKey(user.getId());
            String value=redisUtils.get(key);
            if (value == null) {
                return new EasyResult(1,"认证信息已失效，请重新获取认证信息");
            }
            //认证信息格式转换
            UserAuthenticate authenticateInfo=SecondHandUtil.getObjectFromJson(value,UserAuthenticate.class);
            //保存至数据库
            userAuthenticateService.addAuthenticate(authenticateInfo);
            //修改当前登录用户的认证状态
            user.setAuthenticateId(authenticateInfo.getId());
            //将授权状态同步至数据库
            userService.updateAuthenticateStatus(user);

            //完成认证关联后，从redis中剔除认证信息缓存
            redisUtils.del(key);
            return new EasyResult(0,"认证信息关联成功");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new EasyResult(1,"认证信息关联,请重新按步骤获取jessionId");
        }
    }


    @RequestMapping(value = "member/authenticate",method = RequestMethod.GET)
    public String view(Model model){
        return "member/userAuthenticate";
    }
}
