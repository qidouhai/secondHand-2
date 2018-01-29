package cn.chenny3.secondHand.common.interceptor;

import cn.chenny3.secondHand.model.User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 登录拦截器
 */
@Component
public class LoginInterceptor implements HandlerInterceptor{
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
            //跳转登录页面
            response.sendRedirect("/login");
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
}
