package com.xeubiart.identity.exceptions;

public class IdentityConflictException extends IdentityException {
    public IdentityConflictException(String identifier) {
        super("Identity already exists: " + identifier);
    }
}
