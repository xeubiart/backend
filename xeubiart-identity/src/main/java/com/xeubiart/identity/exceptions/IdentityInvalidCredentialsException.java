package com.xeubiart.identity.exceptions;

import com.xeubiart.core.exceptions.ApplicationException;

public class IdentityInvalidCredentialsException extends ApplicationException {
    public IdentityInvalidCredentialsException() {
        super("Invalid credentials", "ERR_INVALID_CREDENTIALS", 401);
    }
}
