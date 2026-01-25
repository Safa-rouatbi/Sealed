package com.sealed.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// comme ca spring boot va automatiquement renvoyer un 404 Not Found quand i rendcontre cette exeption
@ResponseStatus(HttpStatus.NOT_FOUND)
public class SecretNotFoundException extends RuntimeException {

    public SecretNotFoundException() {
        super("Secret introuvable");
    }
}
