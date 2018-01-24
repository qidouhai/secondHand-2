package cn.chenny3.secondHand.commons.interceptor;

import cn.chenny3.secondHand.commons.vo.ViewObject;
import cn.chenny3.secondHand.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 为有顶部导航的页面注入类目信息
 */
@Component
public class ViewInterceptor implements HandlerInterceptor {
    @Autowired
    private CategoryService categoryService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            ViewObject vo = (ViewObject) modelAndView.getModel().get("vo");
            if (vo == null) {
                vo = new ViewObject();
            }
            vo.put("categories",categoryService.getNavCategories());
            modelAndView.addObject("vo",vo);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
