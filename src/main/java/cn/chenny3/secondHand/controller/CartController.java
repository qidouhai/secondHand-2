package cn.chenny3.secondHand.controller;

import cn.chenny3.secondHand.common.bean.PageHelper;
import cn.chenny3.secondHand.common.bean.UserHolder;
import cn.chenny3.secondHand.common.bean.dto.EasyResult;
import cn.chenny3.secondHand.common.bean.dto.PaginationResult;
import cn.chenny3.secondHand.common.bean.enums.GoodsStatus;
import cn.chenny3.secondHand.model.Cart;
import cn.chenny3.secondHand.model.Goods;
import cn.chenny3.secondHand.service.CartService;
import cn.chenny3.secondHand.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RequestMapping(value = "cart")
@Controller
public class CartController extends BaseController{
    @Autowired
    CartService cartService;
    @Autowired
    UserHolder userHolder;
    @Autowired
    GoodsService goodsService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public EasyResult add(Cart cart){
        try{
            Goods goods = goodsService.selectGoods(cart.getGoodsId());
            if(goods==null){
                return new EasyResult(1,"未查询到商品信息");
            }
            if(goods.getStatus()!= GoodsStatus.PUBLISH){
                return new EasyResult(1,"不能将非发布状态下的商品添加至购物车");
            }
            if(goods.getInventory()<cart.getNum()){
                return new EasyResult(1,"添加商品至购物车失败。添加数量已经超出该商品的库存量。");
            }
            cart.setGoodsId(goods.getId());
            cart.setGoodsName(goods.getGoodsName());
            cart.setGoodsImage(goods.getImageArr()[0]);
            cart.setGoodsPrice(goods.getPrice());
            cart.setUserId(userHolder.get().getId());
            cartService.addCart(cart);
            return new EasyResult(0,"添加购物车成功");
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
            return new EasyResult(1,"添加购物车失败");
        }

    }


    @RequestMapping(value = {"/list"},method = RequestMethod.GET)
    @ResponseBody
    public PaginationResult<Cart> list(){
        int userId = userHolder.get().getId();
        //查询当前用户的购物车记录数
        int count = cartService.selectCartCount(userId);

        List<Cart> carts = Collections.emptyList();
        //只有在记录数不为空的情况下，根据分页情况查询详细的记录信息
        if (count != 0){
            carts=cartService.selectCartList(userId);
        }
        return new PaginationResult<Cart>(count, carts);
    }

}
