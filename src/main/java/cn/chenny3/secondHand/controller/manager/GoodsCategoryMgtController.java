package cn.chenny3.secondHand.controller.manager;

import cn.chenny3.secondHand.common.bean.dto.EasyResult;
import cn.chenny3.secondHand.controller.BaseController;
import cn.chenny3.secondHand.model.Category;
import cn.chenny3.secondHand.service.CategoryService;
import cn.chenny3.secondHand.service.GoodsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/manager/goods/category")
@Controller
public class GoodsCategoryMgtController extends BaseController{
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private GoodsService goodsService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<Category> getCategoryNode(@RequestParam(name = "id",defaultValue = "-1") int parentId){
        List<Category> categoryList = categoryService.selectCategoriesByParentId(parentId);
        return categoryList;
    }

    @RequestMapping(value = "updateName",method = RequestMethod.POST)
    @ResponseBody
    public EasyResult updateCategoryName(Category category){
        if(category==null){
            return new EasyResult(1,"参数不全");
        }
        if(category.getId()<=0||StringUtils.isEmpty(category.getCategoryName())){
            return new EasyResult(1,"类目id小于等于0，或者类目名为空");
        }
        categoryService.updateCategoryName(category);
        return new EasyResult(0,"类目名称修改成功");
    }

    @RequestMapping(value = "canRemove")
    @ResponseBody
    public EasyResult canRemove(@RequestParam int categoryId){
        EasyResult easyResult=null;
        //查询该节点是否包含子节点
        Category category = categoryService.selectCategory(categoryId);
        if(category.getIsParent()==1){
            return new EasyResult(1,"该类目包含子类目，不能删除");
        }
        //查询该节点下是否拥有商品
        int count = goodsService.selectGoodsCountByCategory(categoryId);
        if(count!=0){
            return new EasyResult(1,"该类目包含商品，不能删除");
        }
        return new EasyResult(0,"该类目可以删除");
    }

    @RequestMapping(value = "remove")
    @ResponseBody
    public EasyResult remove(@RequestParam int categoryId){
        EasyResult easyResult = canRemove(categoryId);
        if(easyResult.getCode()==0){

            categoryService.removeCategory(categoryId);
            return new EasyResult(0,"该类目成功删除");
        }else{
            return  easyResult;
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public EasyResult add(@Valid Category category, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                return new EasyResult(1, objectErrorsToString(bindingResult));
            }
            categoryService.addCategory(category);

            return new EasyResult(0, category);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new EasyResult(1, "添加失败");
        }
    }
}
