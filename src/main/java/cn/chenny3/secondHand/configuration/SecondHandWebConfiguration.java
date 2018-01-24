package cn.chenny3.secondHand.configuration;

import cn.chenny3.secondHand.commons.interceptor.LoginInterceptor;
import cn.chenny3.secondHand.commons.interceptor.PassportInterceptor;
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

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor);
        registry.addInterceptor(loginInterceptor).addPathPatterns("/member/**");
        super.addInterceptors(registry);
    }
}
