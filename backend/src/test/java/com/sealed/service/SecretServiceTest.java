package com.sealed.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import com.sealed.dto.CreateSecretRequest;
import com.sealed.dto.SecretResponse;
import com.sealed.repository.SecretRepository;

class SecretServiceTest {

    SecretRepository repository = mock(SecretRepository.class);
    SecretService service = new SecretService(repository, 3600);

    @Test
    void testCreateSecret() {
        CreateSecretRequest request = new CreateSecretRequest("abc", "def", 120);

        String id = service.createSecret(request);

        verify(repository).save(id, "def", "abc", Duration.ofSeconds(120));
    }

    @Test
    void testTtlTropGrand() {
        CreateSecretRequest request = new CreateSecretRequest("abc", "def", 5000);

        assertThrows(IllegalArgumentException.class, () -> {
            service.createSecret(request);
        });
    }

    @Test
    void testIdNonNull() {
        CreateSecretRequest request = new CreateSecretRequest("abc", "def", 120);

        String id = service.createSecret(request);

        assertNotNull(id);
    }

    @Test
    void testGetSecretExistant() {
        when(repository.get("id1")).thenReturn("def:abc");

        SecretResponse response = service.getSecret("id1");

        assertEquals("def", response.getIv());
        assertEquals("abc", response.getCiphertext());
    }

    @Test
    void testGetSecretIntrouvable() {
        when(repository.get("id1")).thenReturn(null);

        assertThrows(SecretNotFoundException.class, () -> {
            service.getSecret("id1");
        });
    }
}