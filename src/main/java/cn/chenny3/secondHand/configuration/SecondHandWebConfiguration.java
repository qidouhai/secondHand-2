package cn.chenny3.secondHand.configuration;

import cn.chenny3.secondHand.common.interceptor.LoginInterceptor;
import cn.chenny3.secondHand.common.interceptor.PassportInterceptor;
import cn.chenny3.secondHand.common.interceptor.ViewInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
@Configuration
public class SecondHandWebConfiguration extends WebMvcConfigurerAdapter{
    @Autowired
    PassportInterceptor passportInterceptor;
    @Autowired
    LoginInterceptor loginInterceptor;
    @Autowired
    ViewInterceptor viewInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(viewInterceptor).excludePathPatterns("/member/*","/login");
        registry.addInterceptor(passportInterceptor);
        registry.addInterceptor(loginInterceptor).addPathPatterns("/member/**","/goods/**").excludePathPatterns("/goods/list/**");
        super.addInterceptors(registry);
    }
}
