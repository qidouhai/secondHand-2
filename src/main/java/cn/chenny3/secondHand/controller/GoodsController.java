package cn.chenny3.secondHand.controller;

import cn.chenny3.secondHand.commons.bean.UserHolder;
import cn.chenny3.secondHand.commons.result.EasyResult;
import cn.chenny3.secondHand.commons.vo.ViewObject;
import cn.chenny3.secondHand.model.Category;
import cn.chenny3.secondHand.model.Goods;
import cn.chenny3.secondHand.model.User;
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
import java.util.List;

@Controller
@RequestMapping("goods")
public class GoodsController extends BaseController{
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserHolder userHolder;
    @Autowired
    private CategoryService categoryService;

    @RequestMapping(value = "addView" ,method = RequestMethod.GET)
    public String addView(Model model){
        return "addGoods";
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public EasyResult addGoods(@Valid Goods goods, BindingResult bindingResult){
        String action=goods.getStatus()==1?"商品信息发布":"商品信息保存";
        try{
            if(bindingResult.hasErrors()){
                return new EasyResult(1,objectErrorsToString(bindingResult));
            }
            goods.setOwnerId(userHolder.get().getId());
            goodsService.addGoods(goods);
            return new EasyResult(0,goods.getId());
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
            return new EasyResult(1,action+"失败");
        }
    }

    @RequestMapping(value = "{id}",method = RequestMethod.GET)
    public String getDetail(@PathVariable int id,Model model){
        try{
            if(id<=0){
                return "redirect:/404.html";
            }
            Goods goods=goodsService.selectGoods(id);
            if(goods!=null){
                //添加访问量
                goods.setViewNum(goods.getViewNum()+1);
                goodsService.updateViewNum(goods.getId(),goods.getViewNum());
                //查询商品归属人
                User owner = userService.selectUser(goods.getOwnerId());
                //抹掉安全相关信息
                owner.setPassword("");
                owner.setSalt("");

                ViewObject viewObject=new ViewObject().
                        put("goods",goods).
                        put("owner",owner).
                        put("topCategory",categoryService.selectCategory(goods.getCategoryId())).
                        put("tag",categoryService.selectCategory(goods.getSubCategoryId()));
                model.addAttribute("vo",viewObject);
                return "shop_detail";
            }
            return "redirect:/404.html";
        }catch (Exception e){
            logger.error(e.getMessage());
            return "redirect:/404.html";
        }
    }
}
