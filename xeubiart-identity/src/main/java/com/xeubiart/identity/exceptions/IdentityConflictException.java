package com.xeubiart.identity.exceptions;

import com.xeubiart.core.exceptions.ApplicationException;

// TODO may remove it
public class IdentityConflictException extends ApplicationException {
    public IdentityConflictException(String identifier) {
        super("Identity already exists: " + identifier, "ERR_IDENTITY_CONFLICT", 409);
    }
}
