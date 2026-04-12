package com.xeubiart.verification.exceptions;

import com.xeubiart.core.exceptions.ApplicationException;

public class BadVerificationException extends ApplicationException {
    public BadVerificationException(String message) {
        super(message, "ERR_BAD_VERIFICATION", 400);
    }
}
