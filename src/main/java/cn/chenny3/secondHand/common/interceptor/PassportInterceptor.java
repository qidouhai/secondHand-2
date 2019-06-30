package cn.chenny3.secondHand.common.interceptor;

import cn.chenny3.secondHand.common.bean.UserHolder;
import cn.chenny3.secondHand.common.bean.vo.ViewObject;
import cn.chenny3.secondHand.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 作用:
 * 1.从Session中将用户会话信息注入到UserHolder持有类中，方便后续获取user信息
 * 2.从Session中将用户会话信息注入到ModelAndView中，方便视图中对user信息的渲染
 */
@Component
public class PassportInterceptor implements HandlerInterceptor{
    @Autowired
    UserHolder userHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        User user= (User) session.getAttribute("user");
        if (user != null) {
            userHolder.set(user);
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if(modelAndView!=null&&userHolder.get()!=null){
            ViewObject vo = (ViewObject) modelAndView.getModel().get("vo");
            if (vo == null) {
                vo = new ViewObject();
            }
            if(vo.get("user")==null){
                vo.put("user",userHolder.get());
            }
            modelAndView.addObject("vo",vo);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        userHolder.clear();
    }
}
