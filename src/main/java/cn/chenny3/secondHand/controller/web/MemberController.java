package cn.chenny3.secondHand.controller.web;

import cn.chenny3.secondHand.commons.bean.PageHelper;
import cn.chenny3.secondHand.commons.bean.UserHolder;
import cn.chenny3.secondHand.commons.enums.ContentType;
import cn.chenny3.secondHand.commons.result.EasyResult;
import cn.chenny3.secondHand.commons.utils.RedisAdapter;
import cn.chenny3.secondHand.commons.utils.RedisKeyUtil;
import cn.chenny3.secondHand.commons.vo.ViewObject;
import cn.chenny3.secondHand.controller.BaseController;
import cn.chenny3.secondHand.model.*;
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
import java.util.ArrayList;
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

    @RequestMapping(value = {"collect", "collect/{curPage}", "collect/{curPage}/{pageSize}"}, method = RequestMethod.GET)
    public String mycollect(@PathVariable(required = false) Integer curPage,
                            @PathVariable(required = false) Integer pageSize,
                            Model model) {
        //当前页，页大小初始化
        if (curPage == null) {
            curPage = 1;
        }
        if (pageSize == null) {
            pageSize = 8;
        }
        //值检测
        if (curPage <= 0 || pageSize <= 0) {
            return "redirect:/404.html";
        }

        //模型数据初始化
        PageHelper<Goods> pageHelper = new PageHelper<>();
        pageHelper.setCurPage(curPage);
        pageHelper.setPageSize(pageSize);
        model.addAttribute("vo",new ViewObject().put("pageHelper",pageHelper));

        //查询出我的收藏中所有商品的id，并按时间倒序排序
        String userCollectKey = RedisKeyUtil.getUserCollectKey(userHolder.get().getId());
        String[] arr = redisAdapter.zrevrange(userCollectKey, 0, -1).toArray(new String[0]);
        //数组为空，直接跳到页面
        if (arr == null || arr.length == 0) {
            return "member/collect";
        }

        //查询内容数量
        int count = arr.length;
        pageHelper.setCount(count);

        //计算起始量
        int start=(curPage-1)*pageSize;
        //查询当前页的所有商品数据
        List<Goods> goodsList = new ArrayList<>();
        for (int i = start; i < count&&i<(start+pageSize); i++) {
            goodsList.add(goodsService.selectGoods(Integer.valueOf(arr[i])));
        }

        pageHelper.setContents(goodsList);

        return "member/collect";
    }

    @RequestMapping("ip")
    @ResponseBody
    public String getIp(HttpServletRequest request) {
        return request.getRemoteAddr();
    }


}
