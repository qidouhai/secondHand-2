package cn.chenny3.secondHand.common.interceptor;

import cn.chenny3.secondHand.common.bean.dto.EasyResult;
import cn.chenny3.secondHand.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 登录拦截器
 */
@Component
public class LoginInterceptor implements HandlerInterceptor{
    private ObjectMapper objectMapper=new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //todo:需改进
        String requestURI = request.getRequestURI();
        String method=request.getMethod();
        if(requestURI.matches("/goods/[0-9]+")&&method.equalsIgnoreCase("get")){
            return true;
        }

        HttpSession session = request.getSession();
        User user= (User) session.getAttribute("user");
        //如果session中不存在用户会话信息
        if (user == null) {
            if(isAjaxRequest(request)){
                //是否访问后台
                if(requestURI.startsWith("/manager")){
                    //跳转到后台登录页面
                    writeString(response,objectMapper.writeValueAsString(new EasyResult(1,"请先在后台登录！")));
                }else{
                    //跳转到前台登录页面
                    writeString(response,objectMapper.writeValueAsString(new EasyResult(1,"请先在前台登录！")));
                }
            }else{
                //是否访问后台
                if(requestURI.startsWith("/manager")){
                    //跳转到后台登录页面
                    response.sendRedirect("/manager/login");
                }else{
                    //跳转到前台登录页面
                    response.sendRedirect("/login");
                }
            }
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    private boolean isAjaxRequest(HttpServletRequest request){
        String headerValue = request.getHeader("X-Requested-With");
        if(headerValue!=null&&headerValue.equals("XMLHttpRequest")){
            return true;
        }else{
            return false;
        }
    }

    private void writeString(HttpServletResponse response,String str){
        try {
            PrintWriter writer = response.getWriter();
            writer.write(str);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
