package com.sealed.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Duration;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RateLimitIntercepteur implements HandlerInterceptor {

    private final StringRedisTemplate redis;
    private final long maxRequests;
    private final long windowSeconds;

    public RateLimitIntercepteur(StringRedisTemplate redis,
            @Value("${sealed.rate.max}") long maxRequests,
            @Value("${sealed.rate.fenetre}") long windowSeconds) {
        this.redis = redis;
        this.maxRequests = maxRequests;
        this.windowSeconds = windowSeconds;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String ip = getClientIp(request);
        String key = "rate:" + ip;
        Long count = redis.opsForValue().increment(key);

        if (count == 1) {
            redis.expire(key, Duration.ofSeconds(windowSeconds));
        }

        if (count > maxRequests) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            response.setHeader("Retry-After", String.valueOf(windowSeconds));
            response.getWriter().write("{\"error\":\"Trop de requêtes, réessayez plus tard\"}");
            return false;
        }

        return true;
    }
    private String getClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}