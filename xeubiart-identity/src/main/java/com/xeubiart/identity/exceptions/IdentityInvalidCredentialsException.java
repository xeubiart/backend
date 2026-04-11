package com.xeubiart.identity.exceptions;

public class IdentityInvalidCredentialsException extends IdentityException {
    public IdentityInvalidCredentialsException() {
        super("Invalid credentials:");
    }
}
