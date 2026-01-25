
package com.sealed.controller;

import java.util.Map;

import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.sealed.dto.CreateSecretRequest;
import com.sealed.dto.CreateSecretResponse;
import com.sealed.dto.SecretResponse;
import com.sealed.service.SecretService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/secrets")
public class SecretController {

    private final SecretService secretService;

    public SecretController(SecretService secretService) {
        this.secretService = secretService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateSecretResponse create(@Valid @RequestBody CreateSecretRequest request) {
        return new CreateSecretResponse(secretService.createSecret(request));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleBadRequest(IllegalArgumentException e) {
        return Map.of("error", e.getMessage());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SecretResponse> get(@PathVariable String id) {
        // no-store : empêche le navigateur et les proxys de garder le secret en cache
        return ResponseEntity.ok()
                .cacheControl(CacheControl.noStore())
                .body(secretService.getSecret(id));
    }
  
}
 