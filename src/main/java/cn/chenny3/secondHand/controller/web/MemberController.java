package cn.chenny3.secondHand.controller.web;

import cn.chenny3.secondHand.common.bean.PageHelper;
import cn.chenny3.secondHand.common.bean.UserHolder;
import cn.chenny3.secondHand.common.bean.dto.PaginationResult;
import cn.chenny3.secondHand.common.utils.RedisUtils;
import cn.chenny3.secondHand.common.utils.RedisKeyUtils;
import cn.chenny3.secondHand.common.bean.vo.ViewObject;
import cn.chenny3.secondHand.controller.BaseController;
import cn.chenny3.secondHand.model.*;
import cn.chenny3.secondHand.service.GoodsService;
import cn.chenny3.secondHand.service.LoginRecordService;
import cn.chenny3.secondHand.service.UserAuthenticateService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    private RedisUtils redisUtils;
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
            pageSize = 9;
        }
        //值检测
        if (curPage <= 0 || pageSize <= 0) {
            return "redirect:/404.html";
        }

        //模型数据初始化
        PageHelper<Goods> pageHelper = new PageHelper<>();
        pageHelper.setCurPage(curPage);
        pageHelper.setPageSize(pageSize);
        model.addAttribute("vo", new ViewObject().put("pageHelper", pageHelper));

        //查询出我的收藏中所有商品的id，并按时间倒序排序
        String userCollectKey = RedisKeyUtils.getUserCollectKey(userHolder.get().getId());
        String[] arr = redisUtils.zrevrange(userCollectKey, 0, -1).toArray(new String[0]);
        //数组为空，直接跳到页面
        if (arr == null || arr.length == 0) {
            return "member/collect";
        }

        //查询内容数量
        int count = arr.length;
        pageHelper.setCount(count);

        //计算起始量
        int start = (curPage - 1) * pageSize;
        //查询当前页的所有商品数据
        List<Goods> goodsList = new ArrayList<>();
        for (int i = start; i < count && i < (start + pageSize); i++) {
            goodsList.add(goodsService.selectGoods(Integer.valueOf(arr[i])));
        }

        pageHelper.setContents(goodsList);

        return "member/collect";
    }

    @RequestMapping(value = "/suggest", method = RequestMethod.GET)
    public String suggest() {
        return "member/suggest";
    }

    @RequestMapping(value = "/safe", method = RequestMethod.GET)
    public String safe() {
        return "member/safe";
    }

    @RequestMapping(value = "/mygoods", method = RequestMethod.GET)
    public String myGoodsView() {
        return "member/mygoods";
    }

    @RequestMapping(value = "/mygoods", method = RequestMethod.POST)
    @ResponseBody
    public PaginationResult myGoods(@RequestParam(value = "pageNumber") Integer curPage,
                                    @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                    @RequestParam(value = "searchText", required = false) String search,
                                    @RequestParam(value = "sortOrder", required = false) String order,
                                    @RequestParam(required = false) Integer status,
                                    Model model) {
        if (curPage == null || curPage <= 0) {
            curPage = 1;
        }
        if (pageSize == null || pageSize <= 0) {
            pageSize = 8;
        }
        if (StringUtils.isBlank(order)) {
            order = "desc";
        } else if (!order.equals("desc") && !order.equals("asc")) {
            order = "desc";
        }
        if (search != null) {
            search = search.trim();
        }
        if (status != null) {
            if (status != 1 && status != 2) {
                status = null;
            }
        }
        int ownerId = userHolder.get().getId();
        int count = goodsService.selectMyGoodsCount(search, status, ownerId);
        List<Goods> rows = Collections.emptyList();
        if (count > 0) {
            rows = goodsService.selectMyGoods(curPage, pageSize, search, order, status, ownerId);
        }
        return new PaginationResult<Goods>(count, rows);
    }


    @RequestMapping("ip")
    @ResponseBody
    public String getIp(HttpServletRequest request) {
        return request.getRemoteAddr();
    }

    /**
     * 用户信息补充页面
     * @param model
     * @return
     */
    @RequestMapping("supplememnt")
    public String supplementView(Model model) {
        return "member/informationSupplement";
    }
}
