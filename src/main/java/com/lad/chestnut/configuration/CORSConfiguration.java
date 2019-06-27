package com.lad.chestnut.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author lad
 * @date 2019/4/26
 */
@Configuration
public class CORSConfiguration {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // 配置可以被跨域的路径，可以任意配置，可以具体到直接请求路径
                registry.addMapping("/**")
                        // 允许所有的请求header访问，可以自定义设置任意请求头信息
                        .allowedHeaders("*")
                        // 允许所有的请求方法访问该跨域资源服务器 "GET", "POST", "DELETE", "PUT"
                        .allowedMethods("*")
                        // 允许所有的请求域名访问我们的跨域资源
                        .allowedOrigins("*");
            }
        };
    }
}
