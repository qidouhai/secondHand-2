package cn.chenny3.secondHand.configuration;

import cn.chenny3.secondHand.common.interceptor.*;
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
    @Autowired
    AuthenticateInterceptor authenticateInterceptor;
    @Autowired
    UserInfoSupplementInterceptor userInfoSupplementInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(viewInterceptor).excludePathPatterns("/member/*","/login");
        registry.addInterceptor(passportInterceptor);
        registry.addInterceptor(authenticateInterceptor).addPathPatterns("/member/**").excludePathPatterns("/member/authenticate","/member/authenticate/confirm");
        registry.addInterceptor(userInfoSupplementInterceptor).addPathPatterns("/member/**").excludePathPatterns("/member/supplement","/member/authenticate","/member/authenticate/confirm");
        registry.addInterceptor(loginInterceptor).addPathPatterns("/member/**","/goods/**").excludePathPatterns("/goods/list/**");
        super.addInterceptors(registry);
    }
}
