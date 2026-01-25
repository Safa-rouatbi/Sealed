package com.sealed.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import java.util.concurrent.Future;


@SpringBootTest
class SecretRepositoryTest {

    private static final String ID = "test-id";

    @Autowired
    SecretRepository repository;

    @Autowired
    StringRedisTemplate redis;

    @AfterEach
    void nettoyer() {
        redis.delete("secret:" + ID);
    }

    @Test
    void testDeuxLecturesSuccessives() {
        repository.save(ID, "iv", "ct", Duration.ofSeconds(60));

        assertEquals("iv:ct", repository.get(ID));
        assertNull(repository.get(ID));
    }

    @Test
    void testCleSupprimeeApresLecture() {
        repository.save(ID, "iv", "ct", Duration.ofSeconds(60));

        repository.get(ID);

        assertFalse(redis.hasKey("secret:" + ID));
    }

    @Test
    void testCleInexistante() {
        assertNull(repository.get("n-existe-pas"));
    }

    @Test
void testDeuxLecturesSimultanees() throws Exception {
    repository.save(ID, "iv", "ct", Duration.ofSeconds(60));

    ExecutorService executor = Executors.newFixedThreadPool(2);
     Future<String> a = executor.submit(() -> repository.get(ID));
    Future<String> b = executor.submit(() -> repository.get(ID));

    int reussies = 0;
    if (a.get() != null) reussies++;
    if (b.get() != null) reussies++;

    assertEquals(1, reussies);
    executor.shutdown();
}

}