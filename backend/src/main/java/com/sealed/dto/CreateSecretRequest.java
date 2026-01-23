
package com.sealed.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateSecretRequest(
        @NotBlank @Size(max = 100_000) @Pattern(regexp = "[A-Za-z0-9_-]+") String ciphertext,
        @NotBlank @Size(max = 32) @Pattern(regexp = "[A-Za-z0-9_-]+") String iv,
        @Min(60) long ttlSeconds) {
}

