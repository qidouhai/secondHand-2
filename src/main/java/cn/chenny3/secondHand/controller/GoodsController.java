package cn.chenny3.secondHand.controller;

import cn.chenny3.secondHand.commons.bean.UserHolder;
import cn.chenny3.secondHand.commons.result.EasyResult;
import cn.chenny3.secondHand.commons.utils.RedisAdapter;
import cn.chenny3.secondHand.commons.utils.RedisKeyUtil;
import cn.chenny3.secondHand.commons.vo.ViewObject;
import cn.chenny3.secondHand.model.Address;
import cn.chenny3.secondHand.model.Goods;
import cn.chenny3.secondHand.model.User;
import cn.chenny3.secondHand.service.AddressService;
import cn.chenny3.secondHand.service.CategoryService;
import cn.chenny3.secondHand.service.GoodsService;
import cn.chenny3.secondHand.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@Controller
@RequestMapping("goods")
public class GoodsController extends BaseController {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserHolder userHolder;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private RedisAdapter redisAdapter;

    @RequestMapping(value = "addView", method = RequestMethod.GET)
    public String addView(Model model) {
        return "addGoods";
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public EasyResult addGoods(@Valid Goods goods, BindingResult bindingResult) {
        String action = goods.getStatus() == 1 ? "商品信息发布" : "商品信息保存";
        try {
            if (bindingResult.hasErrors()) {
                return new EasyResult(1, objectErrorsToString(bindingResult));
            }
            goods.setOwnerId(userHolder.get().getId());
            goodsService.addGoods(goods);
            return new EasyResult(0, goods.getId());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return new EasyResult(1, action + "失败");
        }
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public String getDetail(@PathVariable int id, Model model) {
        try {
            if (id <= 0) {
                return "redirect:/404.html";
            }
            Goods goods = goodsService.selectGoods(id);
            if (goods != null) {
                //添加访问量
                redisAdapter.incr(RedisKeyUtil.getGoodsViewKey(id));
                goods.setViewNum(goods.getViewNum() + 1);
                goodsService.updateViewNum(goods.getId(), 1);
                //查询商品归属人
                User owner = userService.selectUser(goods.getOwnerId());
                //抹掉安全相关信息
                owner.setPassword("");
                owner.setSalt("");
                //查询商品归属人地址信息
                Address address = addressService.select(owner.getAddressId());

                ViewObject viewObject = new ViewObject().
                        put("goods", goods).
                        put("owner", owner).
                        put("address", address).
                        put("topCategory", categoryService.selectCategory(goods.getCategoryId())).
                        put("tag", categoryService.selectCategory(goods.getSubCategoryId()));

                //如果客户端处于登录状态，查询是否收藏、点赞此商品
                if (userHolder.get() != null) {
                    int userId=userHolder.get().getId();
                    viewObject.put("collectFlag",redisAdapter.zrank(RedisKeyUtil.getUserCollectKey(userId),String.valueOf(id))!=null);
                    viewObject.put("hotFlag",redisAdapter.zrank(RedisKeyUtil.getUserHotKey(userId),String.valueOf(id))!=null);
                }
                model.addAttribute("vo", viewObject);
                return "shop_detail";
            }
            return "redirect:/404.html";
        } catch (Exception e) {
            logger.error(e.getMessage());
            return "redirect:/404.html";
        }
    }

    @RequestMapping(value = "/collect/{goodsId}", method = RequestMethod.POST)
    @ResponseBody
    public EasyResult collect(@PathVariable("goodsId") int goodsId) {
        try {
            String userCollectKey = RedisKeyUtil.getUserCollectKey(userHolder.get().getId());
            String goodsCollectKey = RedisKeyUtil.getGoodsCollectKey(goodsId);
            if (redisAdapter.zrank(userCollectKey,String.valueOf(goodsId))!=null) {
                return new EasyResult(1, "不能重复收藏同一件商品");
            }
            //增加收藏量
            redisAdapter.incr(goodsCollectKey);
            goodsService.updateCollectNum(goodsId, 1);
            //保存当前用户的收藏记录
            redisAdapter.zadd(
                    userCollectKey,
                    String.valueOf(goodsId),
                    System.currentTimeMillis()
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
            if (redisAdapter.zrank(userCollectKey,String.valueOf(goodsId))==null) {
                return new EasyResult(1, "不能取消收藏没有收藏过的商品");
            }
            //减少收藏量
            redisAdapter.decr(goodsCollectKey);
            goodsService.updateCollectNum(goodsId, -1);
            //删除当前用户的收藏记录
            redisAdapter.zrem(
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
            if (redisAdapter.zrank(userHotKey, String.valueOf(goodsId))!=null) {
                return new EasyResult(1, "不能重复点赞同一件商品");
            }
            //增加点赞量
            redisAdapter.incr(goodsHotKey);
            goodsService.updateHotNum(goodsId, 1);
            //保存当前用户的点赞记录
            redisAdapter.zadd(
                    userHotKey,
                    String.valueOf(goodsId),
                    System.currentTimeMillis()
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
            if (redisAdapter.zrank(userHotKey, String.valueOf(goodsId))==null) {
                return new EasyResult(1, "不能取消点赞没有点赞过的商品");
            }
            //增加点赞量
            redisAdapter.decr(goodsHotKey);
            goodsService.updateHotNum(goodsId, -1);
            //保存当前用户的点赞记录
            redisAdapter.zrem(
                    userHotKey,
                    String.valueOf(goodsId)
            );
            return new EasyResult(0, "取消点赞成功");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new EasyResult(1, "取消点赞失败");
        }
    }
}
