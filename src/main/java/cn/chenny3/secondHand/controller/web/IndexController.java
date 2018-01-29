package cn.chenny3.secondHand.controller.web;

import cn.chenny3.secondHand.common.bean.enums.ContentType;
import cn.chenny3.secondHand.common.bean.vo.ViewObject;
import cn.chenny3.secondHand.controller.BaseController;
import cn.chenny3.secondHand.model.Category;
import cn.chenny3.secondHand.model.Content;
import cn.chenny3.secondHand.model.Goods;
import cn.chenny3.secondHand.service.CategoryService;
import cn.chenny3.secondHand.service.ContentService;
import cn.chenny3.secondHand.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = {"/index","/"})
public class IndexController extends BaseController{
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private ContentService contentService;
    @Autowired
    private CategoryService categoryService;

    @RequestMapping(method = RequestMethod.GET)
    public String view(Model model){
        //查询banner
        List<Content> banners = contentService.selectContents(ContentType.BANNER, 1, 3);
        //查询官方公告
        List<Content> announcements = contentService.selectContents(ContentType.ANNOUNCEMENT, 1, 7);
        //todo:查询每个分类的标签、热门商品
        List<ViewObject> floors=new ArrayList<ViewObject>();
        for(Category category:categoryService.getNavCategories()){
            ViewObject floor = new ViewObject();
            floor.put("tags",categoryService.selectCategoriesByParentId(category.getId()));
            floor.put("goodsList",goodsService.selectHotGoodsList(category.getId(),7));
            floors.add(floor);
        }
        //查询最新发布的商品
        List<Goods> rencentPublishGoods=goodsService.selectRecentPublishGoods(8);

        ViewObject vo = new ViewObject().
                put("banners",banners).
                put("announcements",announcements).
                put("floors",floors).
                put("rencentPublishGoods",rencentPublishGoods);
        model.addAttribute("vo",vo);
        return "index";
    }
}
