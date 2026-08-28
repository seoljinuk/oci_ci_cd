package com.shopping.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Profile("linux")  // ★ 리눅스에서만 활성화
public class WebConfigLinux implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // file:/images/는 리눅스의 루트 바로 아래의 images 디렉토리를 의미합니다.
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:/images/")
                .setCachePeriod(0);
    }
}