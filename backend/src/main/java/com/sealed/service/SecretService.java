

package com.sealed.service;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sealed.dto.CreateSecretRequest;
import com.sealed.repository.SecretRepository;

@Service
public class SecretService {

    private final SecureRandom secureRandom = new SecureRandom();
    private final SecretRepository secretRepository;
    private final long maxTtlSeconds;

    public SecretService(SecretRepository secretRepository,
          @Value("${sealed.max-ttl-seconds}") long maxTtlSeconds) {
        this.secretRepository = secretRepository;
        this.maxTtlSeconds = maxTtlSeconds;
    }

    public String createSecret(CreateSecretRequest request) {
        if (request.ttlSeconds() > maxTtlSeconds) {
            throw new IllegalArgumentException("ttlSeconds dépasse le maximum de " + maxTtlSeconds);
        }
        String id = generateId();
        secretRepository.save(id, request.iv(), request.ciphertext(), Duration.ofSeconds(request.ttlSeconds()));
        return id;
    }
    private String generateId() {
        byte[] bytes = new byte[16];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}