package com.project.community.config;

import com.project.community.filter.JwtFilter;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Configuration;

@Configuration // indicating that it's for configuration
@RequiredArgsConstructor
public class WebFilterConfig {
    private final JwtFilter jwtFilter;

    // 커스텀 서블릿 필터를 서블릿 컨테이너에 명시적으로 등록
    public FilterRegistrationBean<Filter> jwtFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(jwtFilter); // 필터 인스턴스를 체인에 연결
        filterRegistrationBean.addUrlPatterns("/*"); // 필터가 모든 요청 경로에 적용되도록 한다.
        filterRegistrationBean.setOrder(1); // 필터 실행 순서 지정. 다른 필터보다 우선적으로 실행되도록. 컨트롤러 진입 전 토큰 추출, 검증 수행.
        return filterRegistrationBean;
    }
}
