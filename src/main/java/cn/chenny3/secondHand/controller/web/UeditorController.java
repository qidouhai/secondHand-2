package cn.chenny3.secondHand.controller.web;

import cn.chenny3.secondHand.util.UeditorUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/ueditor")
public class UeditorController {
    @RequestMapping("test")
    public String view(){
        return "ueditor";
    }

    @RequestMapping("config")
    @ResponseBody
    public String getConfigParam(){
        return UeditorUtil.config;
    }




}
