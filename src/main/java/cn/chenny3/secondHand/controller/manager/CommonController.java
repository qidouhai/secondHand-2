package cn.chenny3.secondHand.controller.manager;

import cn.chenny3.secondHand.common.bean.UserHolder;
import cn.chenny3.secondHand.common.bean.vo.ViewObject;
import cn.chenny3.secondHand.model.User;
import cn.chenny3.secondHand.service.LoginRecordService;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.OperatingSystem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

@RequestMapping(value = "/manager")
@Controller
public class CommonController {
    @Autowired
    private UserHolder userHolder;
    @Autowired
    private LoginRecordService loginRecordService;

    //首页
    @RequestMapping(value = "/index")
    public String index() {
        return "/manager/index";
    }
    @RequestMapping(value = "/login")
    public String login(){
        return "/manager/login";
    }

    //用户管理-用户列表
    @RequestMapping(value = "/member-list")
    public String MemberList() {
        return "/manager/member/member-list";
    }
    //用户管理-删除的用户列表
    @RequestMapping(value = "/member-del")
    public String MemberDelList() {
        return "/manager/member/member-del";
    }

    //物品管理-物品种类管理
    @RequestMapping(value = "/goods-category")
    public String goodsCategory() {
        return "/manager/goods/goods-category";
    }
    //物品管理-物品管理
    @RequestMapping(value = "/goods")
    public String goodsList() {
        return "/manager/goods/goods";
    }

    //版图管理-版图列表
    @RequestMapping(value = "/banner-list")
    public String bannerList(){
        return "/manager/banner/banner-list";
    }
    //版图管理-版图添加
    @RequestMapping(value = "/banner-add")
    public String bannerAdd(){
        return "/manager/banner/banner-add";
    }

    //公告管理-公告列表
    @RequestMapping(value = "/announcement-list")
    public String announcementList(){
        return "/manager/announcement/announcement-list";
    }

    //版图管理-公告添加
    @RequestMapping(value = "/announcement-add")
    public String announcementAdd(){
        return "/manager/announcement/announcement-add";
    }

    //建议管理-建议列表
    @RequestMapping(value = "/suggest-list")
    public String suggestList(){
        return "/manager/suggest/suggest-list";
    }

    //欢迎页
    @RequestMapping(value = "/welcome")
    public String welcome(HttpServletRequest request, Model model) throws SigarException {
        User curUser = userHolder.get();
        int userId=curUser.getId();
        Properties props = System.getProperties();
        Runtime runtime = Runtime.getRuntime();
        Locale locale = Locale.getDefault();

        File[] roots = File.listRoots();
        StringBuilder diskStr=new StringBuilder();
        for (int i = 0; i < roots.length; i++) {
            diskStr.append(roots[i]+" ");
        }

        model.addAttribute("vo",new ViewObject()
                .put("lastLoginRecord",loginRecordService.selectLastLoginRecord(userId))//获得上次登录时间
                .put("loginRecordCount",loginRecordService.selectLoginCount(userId))//登录次数
                .put("ipAddress",request.getLocalAddr())//ip地址
                .put("port",request.getServerPort())//端口
                .put("sessionId",request.getRequestedSessionId())//sessionid
                .put("userDir",props.getProperty("user.dir"))//用户当前工作目录
                .put("username",System.getenv("USERNAME"))//主机用户名
                .put("jdkVersion",props.getProperty("java.version"))//jdk版本
                .put("jvmImplName",props.getProperty("java.vm.specification.name"))//jvm实现名称
                .put("jvmImplVersion",props.getProperty("java.vm.version"))//jvm版本
                .put("osName",props.getProperty("os.name"))//os名称
                .put("osVersion",props.getProperty("os.version"))//os版本
                .put("osArch",props.getProperty("os.arch"))//os架构
                .put("memTotal",runtime.totalMemory())//内存总量
                .put("memFree",runtime.freeMemory())//内存剩余量
                .put("country",locale.getCountry())//国家
                .put("language",locale.getLanguage())//语言
                .put("curTime",new Date())//
                .put("processorsCount",runtime.availableProcessors())//语言
                .put("diskInfo",diskStr.toString())
        );
        return "/manager/welcome";
    }

}
