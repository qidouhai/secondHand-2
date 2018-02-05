package cn.chenny3.secondHand.common.interceptor;


import cn.chenny3.secondHand.common.bean.UserHolder;
import cn.chenny3.secondHand.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户身份认证拦截器，如果没有进行身份认证，将会强制跳转到身份认证页面
 */
@Component
public class AuthenticateInterceptor implements HandlerInterceptor {
    @Autowired
    UserHolder userHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = userHolder.get();
        if (user != null) {
            if (user.getAuthenticateId() == 0) {//未认证
                response.sendRedirect("/member/authenticate");
                return false;
            }
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
