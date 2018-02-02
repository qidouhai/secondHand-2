package cn.chenny3.secondHand.controller.web;

import cn.chenny3.secondHand.common.bean.UserHolder;
import cn.chenny3.secondHand.common.bean.dto.EasyResult;
import cn.chenny3.secondHand.controller.BaseController;
import cn.chenny3.secondHand.model.Suggest;
import cn.chenny3.secondHand.service.SuggestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("suggest")
public class SuggestController extends BaseController{
    @Autowired
    private SuggestService suggestService;
    @Autowired
    private UserHolder userHolder;
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public EasyResult add(Suggest suggest) {
        EasyResult easyResult=null;
        try {
            suggest.setUserId(userHolder.get().getId());
            suggestService.addSuggest(suggest);
            easyResult=new EasyResult(0,"建议发表成功");
        } catch (Exception e) {
            logger.error(e.getMessage());
            easyResult=new EasyResult(1,"建议发表失败");
        }
        return easyResult;
    }
}
