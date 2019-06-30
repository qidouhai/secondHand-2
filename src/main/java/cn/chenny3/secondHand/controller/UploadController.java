package cn.chenny3.secondHand.controller;

import cn.chenny3.secondHand.common.bean.dto.EasyResult;
import cn.chenny3.secondHand.common.utils.UeditorUtil;
import cn.chenny3.secondHand.service.OSSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;



@Controller
@RequestMapping("/upload")
public class UploadController extends BaseController{
    @Autowired
    private OSSService ossService;

    @RequestMapping("/img/{type}")
    @ResponseBody
    public Object uploadImg(@PathVariable("type") String type, @RequestPart("img") MultipartFile multipartFile){
        try {
            if("banner".equals(type)){
                String fileName = ossService.saveImage(multipartFile,"img/banner/");
                return new EasyResult(0,fileName);
            }else if("goods".equals(type)){
                String fileName = ossService.saveImage(multipartFile,"img/goods/");
                return new EasyResult(0,fileName);
            }else if("avatar".equals(type)){
                String fileName = ossService.saveImage(multipartFile,"img/avatar/");
                return new EasyResult(0,fileName);
            }else if("ueditor".equals(type)){
                String fileName = ossService.saveImage(multipartFile,"img/other/");
                return new UeditorUtil.UeditorUploadResult(
                        "SUCCESS",
                        "http://secondhand-oss.oss-cn-beijing.aliyuncs.com/"+fileName,
                        fileName.substring(fileName.lastIndexOf("/")),
                        fileName.substring(fileName.lastIndexOf("/"))
                );
            }else{
                String fileName = ossService.saveImage(multipartFile,"img/goods/");
                return new EasyResult(0,fileName);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new EasyResult(1,"上传错误，"+e.getMessage());
        }
    }
}
