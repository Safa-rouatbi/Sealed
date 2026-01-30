package com.sealed.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest 
@AutoConfigureMockMvc
class RateLimitTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    StringRedisTemplate redis;

    @AfterEach
    void clean() { 
        redis.delete("rate:10.0.0.1");
        redis.delete("rate:10.0.0.2");
    }

    @Test
    void testSousLaLimite() throws Exception {
        for (int i = 0; i < 20; i++) { 
            int status = mockMvc.perform(get("/api/secrets/abc")
                    .with(req -> { req.setRemoteAddr("10.0.0.1"); return req; }))
                    .andReturn().getResponse().getStatus();
            assertEquals(404, status);
        }
    }

    @Test
    void testAuDelaDeLaLimite() throws Exception { 
        for (int i = 0; i < 20; i++) {
            mockMvc.perform(get("/api/secrets/abc").with(req -> { req.setRemoteAddr("10.0.0.1"); return req; }));
        }
        
        int status = mockMvc.perform(get("/api/secrets/abc")
         .with(req -> { req.setRemoteAddr("10.0.0.1"); return req; }))
        .andReturn().getResponse().getStatus();
                
        assertEquals(429, status);
    }

    @Test
    void testIpDifferentePasBloquee() throws Exception { 
        for (int i = 0; i < 20; i++) {
            mockMvc.perform(get("/api/secrets/abc").with(req -> { req.setRemoteAddr("10.0.0.1"); return req; }));
                   }

        int status = mockMvc.perform(get("/api/secrets/abc")
         .with(req -> { req.setRemoteAddr("10.0.0.2"); return req; }))
               .andReturn().getResponse().getStatus();

        assertEquals(404, status);
    }

 
}