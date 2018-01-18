package cn.chenny3.secondHand.controller.web;

import cn.chenny3.secondHand.bean.ContentType;
import cn.chenny3.secondHand.bean.PageHelper;
import cn.chenny3.secondHand.bean.ViewObject;
import cn.chenny3.secondHand.controller.BaseController;
import cn.chenny3.secondHand.model.Category;
import cn.chenny3.secondHand.model.Content;
import cn.chenny3.secondHand.service.CategoryService;
import cn.chenny3.secondHand.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class AnnouncementController extends BaseController{
    @Autowired
    ContentService contentService;

    @RequestMapping(value ={"announcement/list","announcement/list/{curPage}","announcement/list/{curPage}/{pageSize}"}, method = RequestMethod.GET)
    public String list(@PathVariable(required = false) Integer curPage,
                       @PathVariable(required = false) Integer pageSize,
                       Model model) {
        //当前页，页大小初始化
        if (curPage == null) {
            curPage = 1;
        }
        if (pageSize == null) {
            pageSize = 5;
        }
        //值检测
        if (curPage <= 0 || pageSize <= 0) {
            return "redirect:/404.html";
        }

        //模型数据初始化
        PageHelper<Content> pageHelper = new PageHelper<>();
        pageHelper.setCurPage(curPage);
        pageHelper.setPageSize(pageSize);
        ViewObject vo = new ViewObject();
        vo.put("pageHelper",pageHelper);
        model.addAttribute("vo",vo);

        //头部导航
        vo.put("categories",getNavCategories());
        //查询内容数量
        int count = contentService.selectCount(ContentType.ANNOUNCEMENT);
        pageHelper.setCount(count);

        if(count==0){
            return "article_list";
        }
        List<Content> contents = contentService.selectContents(ContentType.ANNOUNCEMENT, curPage, pageSize);
        pageHelper.setContents(contents);


        return "article_list";
    }

    @RequestMapping("announcement/{id}")
    public String detail(@PathVariable int id, Model model){
        ViewObject vo = new ViewObject();
        //头部导航
        vo.put("categories",getNavCategories());

        Content announcement = contentService.selectContent(id);
        if(announcement==null){
            return "redirect:/404.html";
        }
        vo.put("announcement",announcement);
        model.addAttribute("vo",vo);
        return "article_detail";
    }
}
