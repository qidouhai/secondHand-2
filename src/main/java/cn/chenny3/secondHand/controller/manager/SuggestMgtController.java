package cn.chenny3.secondHand.controller.manager;

import cn.chenny3.secondHand.Constants;
import cn.chenny3.secondHand.common.bean.dto.EasyResult;
import cn.chenny3.secondHand.common.utils.SecondHandUtil;
import cn.chenny3.secondHand.model.Suggest;
import cn.chenny3.secondHand.service.SuggestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/manager/suggest")
@Controller
public class SuggestMgtController {
    @Autowired
    private SuggestService suggestService;

    @RequestMapping(value = "list",method = RequestMethod.GET)
    @ResponseBody
    public Map list(@RequestParam(name = "start") int start, @RequestParam(name = "length") int offset){
        Map<String,Object> suggestMap=null;
        List<Map<String,Object>> suggestMapList=new ArrayList<>();
        //查询用户数量
        int count = suggestService.selectSuggestCountByMgt();
        if(count!=0){
            List<Suggest> suggestList = suggestService.selectSuggestListByMgt(start,offset);
            //重新封装数据
            if (suggestList != null) {
                for (Suggest suggest : suggestList) {
                    suggestMap=new HashMap<>();
                    suggestMap.put("id",suggest.getId());
                    suggestMap.put("title",suggest.getTitle());
                    suggestMap.put("content",suggest.getContent());
                    suggestMap.put("userId",suggest.getUser().getId());
                    suggestMap.put("userName",suggest.getUser().getName());
                    suggestMap.put("phone",suggest.getUser().getPhone());
                    suggestMap.put("email",suggest.getUser().getEmail());
                    suggestMap.put("createTime", SecondHandUtil.date2String("yyyy-MM-dd HH:mm:ss",suggest.getCreated()));
                    suggestMap.put("status",suggest.getStatus());
                    suggestMapList.add(suggestMap);
                }
            }
        }
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("data",suggestMapList);
        resultMap.put("recordsTotal",count);
        resultMap.put("recordsFiltered",count);
        return resultMap;
    }


    @RequestMapping(value = "delete",method = RequestMethod.POST)
    @ResponseBody
    public EasyResult deleteSuggest(@RequestParam int[] ids){
        try{
            suggestService.batchUpdateStatus(ids, Constants.ENTITY_STATUS_DELETE);
            return new EasyResult(0,"删除成功");
        }catch (Exception e){
            e.printStackTrace();
            return new EasyResult(1,"删除失败");
        }
    }
}
