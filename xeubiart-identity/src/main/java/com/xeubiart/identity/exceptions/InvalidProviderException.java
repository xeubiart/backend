package com.xeubiart.identity.exceptions;

public class InvalidProviderException extends IdentityException {
    public InvalidProviderException(String providerName) {
        super("Invalid provider: " + providerName);
    }
}
