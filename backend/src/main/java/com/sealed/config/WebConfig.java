package com.sealed.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final RateLimitIntercepteur rateLimitIntercepteur;

    public WebConfig(RateLimitIntercepteur rateLimitIntercepteur) {
        this.rateLimitIntercepteur = rateLimitIntercepteur;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(rateLimitIntercepteur).addPathPatterns("/api/**");
    }
}