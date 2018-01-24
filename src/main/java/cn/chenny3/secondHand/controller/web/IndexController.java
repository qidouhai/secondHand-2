package cn.chenny3.secondHand.controller.web;

import cn.chenny3.secondHand.commons.enums.ContentType;
import cn.chenny3.secondHand.commons.vo.ViewObject;
import cn.chenny3.secondHand.controller.BaseController;
import cn.chenny3.secondHand.model.Category;
import cn.chenny3.secondHand.model.Content;
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

    @RequestMapping(method = RequestMethod.GET)
    public String view(Model model){
        ViewObject vo = new ViewObject();
        List<Category> categories = categoryService.selectCategoriesByParentId(0);
        vo.put("categories",getNavCategories());
        //查询banner
        List<Content> banners = contentService.selectContents(ContentType.BANNER, 1, 3);
        vo.put("banners",banners);
        //查询官方公告
        List<Content> announcements = contentService.selectContents(ContentType.ANNOUNCEMENT, 1, 7);
        vo.put("announcements",announcements);
        List<ViewObject> floors=new ArrayList<ViewObject>();
        for(Category category:categories){
            ViewObject floor = new ViewObject();
            floor.put("tags",categoryService.selectCategoriesByParentId(category.getId()));
            floor.put("goods",goodsService.selectHotGoodsList(category.getId(),9));
            floors.add(floor);
        }
        vo.put("floors",floors);
        model.addAttribute("vo",vo);
        return "index";
    }
}
