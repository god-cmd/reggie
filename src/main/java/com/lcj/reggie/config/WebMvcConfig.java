package com.lcj.reggie.config;/*
    @author lcj
    @create -
*/

import com.lcj.reggie.common.JacksonObjectMapper;
import com.lcj.reggie.interceptor.LoginCheckInterceptor;
import com.lcj.reggie.interceptor.FLoginCheckInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@Slf4j
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("开始处理资源映射");
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        log.info("资源映射处理完成");
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        converters.add(0,messageConverter);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/user/sendMsg",
                "/user/login"
        };
        LoginCheckInterceptor loginCheckInterceptor = new LoginCheckInterceptor();
        registry.addInterceptor(loginCheckInterceptor)
                .addPathPatterns("/**").excludePathPatterns(urls);
    }
}
