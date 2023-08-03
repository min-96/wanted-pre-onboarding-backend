package com.example.wantedpreonboardingbackend.config;

import com.example.wantedpreonboardingbackend.util.interceptor.ValidInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfiguration implements WebMvcConfigurer {

    @Bean
    public ValidInterceptor validInterceptor() {
        return new ValidInterceptor();
    }


    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(validInterceptor())
                .excludePathPatterns("/signup", "/signin");
    }

}
