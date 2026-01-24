package com.sealed.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.sealed.service.SecretService;

@WebMvcTest(SecretController.class)
class SecretControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    SecretService secretService;

    @Test
    void testCreate() throws Exception {
        when(secretService.createSecret(any())).thenReturn("id123");

        String json = "{\"ciphertext\":\"abc\",\"iv\":\"def\",\"ttlSeconds\":120}";

        mockMvc.perform(post("/api/secrets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("id123"));
    }

    @Test
    void testCiphertextVide() throws Exception {
        String json = "{\"ciphertext\":\"\",\"iv\":\"def\",\"ttlSeconds\":120}";

        mockMvc.perform(post("/api/secrets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testIvInvalide() throws Exception {
        String json = "{\"ciphertext\":\"abc\",\"iv\":\"de+f\",\"ttlSeconds\":120}";

        mockMvc.perform(post("/api/secrets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testTtlTropPetit() throws Exception {
        String json = "{\"ciphertext\":\"abc\",\"iv\":\"def\",\"ttlSeconds\":10}";

        mockMvc.perform(post("/api/secrets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }
}