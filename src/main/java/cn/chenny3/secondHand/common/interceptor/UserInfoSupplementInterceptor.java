package cn.chenny3.secondHand.common.interceptor;

import cn.chenny3.secondHand.common.bean.UserHolder;
import cn.chenny3.secondHand.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户信息补全拦截器
 */
@Component
public class UserInfoSupplementInterceptor implements HandlerInterceptor{
    @Autowired
    private UserHolder userHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = userHolder.get();
        if (user == null) {
            return true;
        }
        if(user.getAddressId()==0||
                StringUtils.isBlank(user.getQq())||
                StringUtils.isBlank(user.getWechat())||
                StringUtils.isBlank(user.getAlipay())){
            response.sendRedirect("/member/supplement");
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
