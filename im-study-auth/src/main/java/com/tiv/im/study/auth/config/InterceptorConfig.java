package com.tiv.im.study.auth.config;

import com.tiv.im.study.auth.interceptor.JwtInterceptor;
import com.tiv.im.study.auth.interceptor.RequestSourceInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * 拦截器配置类
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Resource
    private RequestSourceInterceptor requestSourceInterceptor;

    @Resource
    private JwtInterceptor jwtInterceptor;

    /**
     * 注册拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestSourceInterceptor)
                .addPathPatterns("/**");
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns(Arrays.asList("/api/v1/auth/user/update/info"));
    }

}
