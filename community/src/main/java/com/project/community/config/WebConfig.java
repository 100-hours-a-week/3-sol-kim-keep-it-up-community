package com.project.community.config;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
// 출처: https://itconquest.tistory.com/entry/Spring-Boot-WebMvcConfigurer-이해하기 [개발자일지:티스토리]

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${file.path}") String uploadDir; // application.yml에서 지정한 런타임에 파일을 저장하는 경로

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + uploadDir)
                .setCachePeriod(3600);
    }
}