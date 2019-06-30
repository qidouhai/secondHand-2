package cn.chenny3.secondHand.controller.manager;

import cn.chenny3.secondHand.Constants;
import cn.chenny3.secondHand.common.bean.dto.EasyResult;
import cn.chenny3.secondHand.common.bean.enums.ContentType;
import cn.chenny3.secondHand.common.bean.vo.ViewObject;
import cn.chenny3.secondHand.common.utils.SecondHandUtil;
import cn.chenny3.secondHand.controller.BaseController;
import cn.chenny3.secondHand.model.Content;
import cn.chenny3.secondHand.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/manager/content")
@Controller
public class ContentMgtController extends BaseController{
    @Autowired
    private ContentService contentService;

    @RequestMapping(value = "banner/list",method = RequestMethod.GET)
    @ResponseBody
    public Map bannerList(@RequestParam(name = "start") int start, @RequestParam(name = "length") int offset){
        Map<String,Object> bannerMap=null;
        List<Map<String,Object>> bannerMapList=new ArrayList<>();
        int count = contentService.selectCountByMgt(ContentType.BANNER,new int[]{Constants.CONTENT_STATUS_PUBLISH,Constants.CONTENT_STATUS_ONLY_SAVE});
        if(count!=0){
            List<Content> bannerList = contentService.selectContentsByMgt(ContentType.BANNER,new int[]{Constants.CONTENT_STATUS_PUBLISH,Constants.CONTENT_STATUS_ONLY_SAVE},start,offset);
            //重新封装数据
            if (bannerList != null) {
                for (Content content : bannerList) {
                    bannerMap=new HashMap<>();
                    bannerMap.put("id",content.getId());
                    bannerMap.put("title",content.getTitle());
                    bannerMap.put("image",content.getImage());
                    bannerMap.put("createTime", SecondHandUtil.date2String("yyyy-MM-dd HH:mm:ss",content.getCreated()));
                    bannerMap.put("status",content.getStatus());
                    bannerMapList.add(bannerMap);
                }
            }
        }
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("data",bannerMapList);
        resultMap.put("recordsTotal",count);
        resultMap.put("recordsFiltered",count);
        return resultMap;
    }

    @RequestMapping(value = "announcement/list",method = RequestMethod.GET)
    @ResponseBody
    public Map AnnouncementList(@RequestParam(name = "start") int start, @RequestParam(name = "length") int offset){
        Map<String,Object> announcementMap=null;
        List<Map<String,Object>> announcementMapList=new ArrayList<>();
        int count = contentService.selectCountByMgt(ContentType.ANNOUNCEMENT,new int[]{Constants.CONTENT_STATUS_PUBLISH,Constants.CONTENT_STATUS_ONLY_SAVE});
        if(count!=0){
            List<Content> announcementList = contentService.selectContentsByMgt(ContentType.ANNOUNCEMENT,new int[]{Constants.CONTENT_STATUS_PUBLISH,Constants.CONTENT_STATUS_ONLY_SAVE},start,offset);
            //重新封装数据
            if (announcementList != null) {
                for (Content content : announcementList) {
                    announcementMap=new HashMap<>();
                    announcementMap.put("id",content.getId());
                    announcementMap.put("title",content.getTitle());
                    announcementMap.put("content",content.getContent());
                    announcementMap.put("createTime", SecondHandUtil.date2String("yyyy-MM-dd HH:mm:ss",content.getCreated()));
                    announcementMap.put("status",content.getStatus());
                    announcementMapList.add(announcementMap);
                }
            }
        }
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("data",announcementMapList);
        resultMap.put("recordsTotal",count);
        resultMap.put("recordsFiltered",count);
        return resultMap;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public EasyResult addOrUpdateContent(@Valid Content content, BindingResult bindingResult) {
        EasyResult easyResult = null;
        try {
            if (bindingResult.hasErrors()) {
                easyResult = new EasyResult(1,objectErrorsToString(bindingResult));
            }
            contentService.addOrUpdateContent(content);
            easyResult = new EasyResult(0,String.valueOf(content.getId()));
        } catch (Exception e) {
            logger.error(e.getMessage());
            easyResult = new EasyResult(1,"保存错误");
        }
        return easyResult;
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public String preUpdateContent(@PathVariable int id, Model model){
        Content content = contentService.selectContent(id);
        if(content!=null){
            model.addAttribute("vo",new ViewObject().put("content",content));
            if(ContentType.BANNER==content.getEntityType()){
                return "/manager/banner/banner-add";
            }else{
                return "/manager/announcement/announcement-add";
            }
        }else{
            return "/404";
        }
    }

    @RequestMapping(value = "delete",method = RequestMethod.POST)
    @ResponseBody
    public EasyResult deleteContent(@RequestParam int ids[]){
        try{
            contentService.batchUpdateStatus(ids,Constants.ENTITY_STATUS_DELETE);
            return new EasyResult(0,"删除成功");
        }catch (Exception e){
            return new EasyResult(1,"删除失败");
        }
    }

    @RequestMapping(value = "publish/{id}",method = RequestMethod.POST)
    @ResponseBody
    public EasyResult publishContent(@PathVariable int id){
        try{
            contentService.updateStatus(id, Constants.CONTENT_STATUS_PUBLISH);
            return new EasyResult(0,"发布成功");
        }catch (Exception e){
            return new EasyResult(1,"发布失败");
        }
    }


    @RequestMapping(value = "cancelPublish/{id}",method = RequestMethod.POST)
    @ResponseBody
    public EasyResult cancelPublishContent(@PathVariable int id){
        try{
            contentService.updateStatus(id, Constants.CONTENT_STATUS_ONLY_SAVE);
            return new EasyResult(0,"取消发布成功");
        }catch (Exception e){
            return new EasyResult(1,"取消发布失败");
        }
    }
}
