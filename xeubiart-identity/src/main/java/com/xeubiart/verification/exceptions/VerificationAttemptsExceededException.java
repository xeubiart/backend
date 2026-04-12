package com.xeubiart.verification.exceptions;

import com.xeubiart.core.exceptions.ApplicationException;

public class VerificationAttemptsExceededException extends ApplicationException {
    public VerificationAttemptsExceededException() {
        super("Verification attempts exceeded. Please try again later.", "ERR_VERIFICATION_ATTEMPTS_EXCEEDED", 429);
    }
}
