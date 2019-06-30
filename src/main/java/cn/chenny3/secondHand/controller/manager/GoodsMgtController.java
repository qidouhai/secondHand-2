package cn.chenny3.secondHand.controller.manager;

import cn.chenny3.secondHand.Constants;
import cn.chenny3.secondHand.common.bean.dto.EasyResult;
import cn.chenny3.secondHand.common.bean.enums.ContentType;
import cn.chenny3.secondHand.common.bean.vo.ViewObject;
import cn.chenny3.secondHand.common.utils.SecondHandUtil;
import cn.chenny3.secondHand.model.Content;
import cn.chenny3.secondHand.model.Goods;
import cn.chenny3.secondHand.service.CategoryService;
import cn.chenny3.secondHand.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/manager/goods")
@Controller
public class GoodsMgtController {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private CategoryService categoryService;

    @RequestMapping(value = "list",method = RequestMethod.GET)
    @ResponseBody
    public Map list(@RequestParam(name = "categoryId",defaultValue = "0") int categoryId,
                    @RequestParam(name = "subCategoryId",defaultValue = "0") int subCategoryId,
                    @RequestParam(name="status",defaultValue = "-1") int status,
                    @RequestParam(name="goodsName",defaultValue = "") String goodsName,
                    @RequestParam(name="startTime",defaultValue = "") String startTime,
                    @RequestParam(name="endTime",defaultValue = "") String endTime,
                    @RequestParam(name = "start") int start,
                    @RequestParam(name = "length") int offset){
        Map<String,Object> goodsMap=null;
        List<Map<String,Object>> goodsMapList=new ArrayList<>();
        //查询用户数量
        int count = goodsService.selectGoodsCountByMgt(categoryId,subCategoryId,status,goodsName,startTime,endTime);
        if(count!=0){
            List<Goods> goodsList = goodsService.selectGoodsListByMgt(categoryId,subCategoryId,status,goodsName,startTime,endTime,start,offset);
            //重新封装数据
            if (goodsList != null) {
                for (Goods goods : goodsList) {
                    goodsMap=new HashMap<>();
                    goodsMap.put("id",goods.getId());
                    goodsMap.put("name",goods.getGoodsName());
                    goodsMap.put("categoryName",goods.getCategoryName());
                    goodsMap.put("subCategoryName",goods.getSubCategoryName());
                    goodsMap.put("price",goods.getPrice());
                    goodsMap.put("image",goods.getImageArr()!=null?goods.getImageArr()[0]:"");
                    goodsMap.put("userId",goods.getOwnerId());
                    goodsMap.put("userName",goods.getOwnerName());
                    goodsMap.put("createTime", SecondHandUtil.date2String("yyyy-MM-dd HH:mm:ss",goods.getCreated()));
                    goodsMap.put("status",goods.getStatus());
                    goodsMapList.add(goodsMap);
                }
            }
        }
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("data",goodsMapList);
        resultMap.put("recordsTotal",count);
        resultMap.put("recordsFiltered",count);
        return resultMap;
    }


    @RequestMapping(value = "delete",method = RequestMethod.POST)
    @ResponseBody
    public EasyResult deleteGoods(@RequestParam int ids[]){
        try{
            goodsService.batchUpdateStatus(ids, Constants.ENTITY_STATUS_DELETE);
            return new EasyResult(0,"删除成功");
        }catch (Exception e){
            return new EasyResult(1,"删除失败");
        }
    }

    @RequestMapping(value = "publish",method = RequestMethod.POST)
    @ResponseBody
    public EasyResult publishGoods(@RequestParam int ids[]){
        try{
            goodsService.batchUpdateStatus(ids, Constants.GOODS_STATUS_PUBLISH);
            return new EasyResult(0,"发布成功");
        }catch (Exception e){
            return new EasyResult(1,"发布失败");
        }
    }


    @RequestMapping(value = "cancelPublish",method = RequestMethod.POST)
    @ResponseBody
    public EasyResult cancelPublishGoods(@RequestParam int ids[]){
        try{
            goodsService.batchUpdateStatus(ids, Constants.GOODS_STATUS_ONLY_SAVE);
            return new EasyResult(0,"取消发布成功");
        }catch (Exception e){
            return new EasyResult(1,"取消发布失败");
        }
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public String preUpdateGoods(@PathVariable int id, Model model){
        Goods goods = goodsService.selectGoods(id);
        if(goods!=null){
            model.addAttribute("vo",new ViewObject().put("goods",goods).put("subCategories",categoryService.selectCategoriesByParentId(goods.getCategoryId())));
                return "/manager/goods/goods-edit";
        }else{
            return "/404";
        }
    }

    @RequestMapping(value = "preview/{id}",method = RequestMethod.GET)
    public String previewGoods(@PathVariable int id, Model model){
        Goods goods = goodsService.selectGoods(id);
        if(goods!=null){
            model.addAttribute("vo",new ViewObject().put("goods",goods));
            return "/manager/goods/goods-preview";
        }else{
            return "/404";
        }
    }


}
