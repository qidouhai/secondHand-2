package cn.chenny3.secondHand.controller.web;

import cn.chenny3.secondHand.commons.bean.UserHolder;
import cn.chenny3.secondHand.commons.result.EasyResult;
import cn.chenny3.secondHand.commons.utils.RedisAdapter;
import cn.chenny3.secondHand.commons.utils.RedisKeyUtil;
import cn.chenny3.secondHand.commons.vo.ViewObject;
import cn.chenny3.secondHand.controller.BaseController;
import cn.chenny3.secondHand.model.Goods;
import cn.chenny3.secondHand.model.LoginRecord;
import cn.chenny3.secondHand.model.User;
import cn.chenny3.secondHand.model.UserAuthenticate;
import cn.chenny3.secondHand.service.AddressService;
import cn.chenny3.secondHand.service.GoodsService;
import cn.chenny3.secondHand.service.LoginRecordService;
import cn.chenny3.secondHand.service.UserAuthenticateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("member")
public class MemberController extends BaseController {
    @Autowired
    private LoginRecordService loginRecordService;
    @Autowired
    private UserAuthenticateService authenticateService;
    @Autowired
    private RedisAdapter redisAdapter;
    @Autowired
    private GoodsService goodsService;
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
    @RequestMapping(value = "/collect/{goodsId}", method = RequestMethod.POST)
    @ResponseBody
    public EasyResult collect(@PathVariable("goodsId") int goodsId) {
        try {
            String userCollectKey = RedisKeyUtil.getUserCollectKey(userHolder.get().getId());
            String goodsCollectKey = RedisKeyUtil.getGoodsCollectKey(goodsId);
            if (redisAdapter.sIsMember(userCollectKey, String.valueOf(goodsId))) {
                return new EasyResult(1, "不能重复收藏同一件商品");
            }
            //增加收藏量
            redisAdapter.incr(goodsCollectKey);
            goodsService.updateCollectNum(goodsId, 1);
            //保存当前用户的收藏记录
            redisAdapter.sadd(
                    userCollectKey,
                    String.valueOf(goodsId)
            );
            return new EasyResult(0, "收藏成功");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new EasyResult(1, "收藏失败");
        }
    }

    @RequestMapping(value = "/collect/{goodsId}", method = RequestMethod.DELETE)
    @ResponseBody
    public EasyResult cancalCollect(@PathVariable("goodsId") int goodsId) {
        try {
            String userCollectKey = RedisKeyUtil.getUserCollectKey(userHolder.get().getId());
            String goodsCollectKey = RedisKeyUtil.getGoodsCollectKey(goodsId);
            if (!redisAdapter.sIsMember(userCollectKey, String.valueOf(goodsId))) {
                return new EasyResult(1, "不能取消收藏没有收藏过的商品");
            }
            //减少收藏量
            redisAdapter.decr(goodsCollectKey);
            goodsService.updateCollectNum(goodsId, -1);
            //保存当前用户的收藏记录
            redisAdapter.srem(
                    userCollectKey,
                    String.valueOf(goodsId)
            );
            return new EasyResult(0, "取消收藏成功");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new EasyResult(1, "取消收藏失败");
        }
    }

    @RequestMapping(value = "/hot/{goodsId}", method = RequestMethod.POST)
    @ResponseBody
    public EasyResult hot(@PathVariable("goodsId") int goodsId) {
        try {
            String userHotKey = RedisKeyUtil.getUserHotKey(userHolder.get().getId());
            String goodsHotKey = RedisKeyUtil.getGoodsHotKey(goodsId);
            if (redisAdapter.sIsMember(userHotKey, String.valueOf(goodsId))) {
                return new EasyResult(1, "不能重复点赞同一件商品");
            }
            //增加点赞量
            redisAdapter.incr(goodsHotKey);
            goodsService.updateHotNum(goodsId, 1);
            //保存当前用户的点赞记录
            redisAdapter.sadd(
                    userHotKey,
                    String.valueOf(goodsId)
            );
            return new EasyResult(0, "点赞成功");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new EasyResult(1, "点赞失败");
        }
    }

    @RequestMapping(value = "/hot/{goodsId}", method = RequestMethod.DELETE)
    @ResponseBody
    public EasyResult cancalHot(@PathVariable("goodsId") int goodsId) {
        try {
            String userHotKey = RedisKeyUtil.getUserHotKey(userHolder.get().getId());
            String goodsHotKey = RedisKeyUtil.getGoodsHotKey(goodsId);
            if (!redisAdapter.sIsMember(userHotKey, String.valueOf(goodsId))) {
                return new EasyResult(1, "不能取消点赞没有点赞过的商品");
            }
            //增加点赞量
            redisAdapter.decr(goodsHotKey);
            goodsService.updateHotNum(goodsId, -1);
            //保存当前用户的点赞记录
            redisAdapter.srem(
                    userHotKey,
                    String.valueOf(goodsId)
            );
            return new EasyResult(0, "取消点赞成功");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new EasyResult(1, "取消点赞失败");
        }
    }
    @RequestMapping(value = "collect", method = RequestMethod.GET)
    public String mycollect(Model model) {
        String userCollectKey = RedisKeyUtil.getUserCollectKey(userHolder.get().getId());
        String[] arr =redisAdapter.scard(userCollectKey).toArray(new String[0]);

        if (arr == null || arr.length == 0) {
            return "member/collect";
        }
        Integer[] goodsIds = new Integer[arr.length];
        //类型转换
        for(int i=0;i<arr.length;i++){
            goodsIds[i]=Integer.valueOf(arr[i]);
        }
        List<Goods> goodsList = goodsService.selectGoodsList(goodsIds);
        ViewObject viewObject=new ViewObject().put("goodsList",goodsList);
        model.addAttribute("vo",viewObject);
        return "member/collect";
    }

    @RequestMapping("ip")
    @ResponseBody
    public String getIp(HttpServletRequest request) {
        return request.getRemoteAddr();
    }


}
