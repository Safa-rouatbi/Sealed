package com.sealed.dto;

public class SecretResponse {

    private String iv;
    private String ciphertext;

    public SecretResponse(String iv, String ciphertext) {
        this.iv = iv;
        this.ciphertext = ciphertext;
    }

    public String getIv() {
        return iv;
    }

    public String getCiphertext() {
        return ciphertext;
    }
}