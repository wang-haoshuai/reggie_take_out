package com.itheima.reggie.config;

import com.itheima.reggie.common.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * mvc配置类
 *
 * @author anfen
 * @date 2022/07/02
 */
@Slf4j
@Configuration
public class MvcConfig implements WebMvcConfigurer {
    /**
     * 设置静态资源路径
     *
     * @param registry 注册表
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
      log.info("设置静态资源路径");
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }

    /**
     * 添加拦截器
     *
     * @param registry 注册表
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration interceptor = registry.addInterceptor(new ProjectInterceptor());
        interceptor.addPathPatterns("/**");
        interceptor.excludePathPatterns(
                "/employee/login",
                "/employee/logout",
                "/front/**",
                "/backend/**",
                "/error"
                );
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter httpMessageConverter =
                new MappingJackson2HttpMessageConverter(new JacksonObjectMapper());
        converters.add(0,httpMessageConverter);
    }
}
