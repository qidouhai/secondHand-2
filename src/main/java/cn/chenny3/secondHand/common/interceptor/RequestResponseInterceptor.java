package cn.chenny3.secondHand.common.interceptor;

import cn.chenny3.secondHand.common.bean.RequestResponseHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 保存当前请求的request,response
 */
@Component
public class RequestResponseInterceptor implements HandlerInterceptor{
    @Autowired
    RequestResponseHolder requestResponseHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        requestResponseHolder.setRequest(request);
        requestResponseHolder.setResponse(response);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        requestResponseHolder.clear();
    }
}
