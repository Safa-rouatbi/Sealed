package com.sealed.repository;

import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SecretRepository {

    private static final String KEY_PREFIX = "secret:";

    private final StringRedisTemplate redis;

    public SecretRepository(StringRedisTemplate redis) {
        this.redis = redis;
    }
    public void save(String id, String iv, String ciphertext, Duration ttl) {
        redis.opsForValue().set(KEY_PREFIX + id, iv + ":" + ciphertext, ttl);
    }

    public String get(String id) {
    String value = redis.opsForValue().getAndDelete("secret:" + id);
    return value;
}
}

