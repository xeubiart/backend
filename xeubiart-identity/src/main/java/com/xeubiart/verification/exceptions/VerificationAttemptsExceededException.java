package com.xeubiart.verification.exceptions;

public class VerificationAttemptsExceededException extends VerificationException {
    public VerificationAttemptsExceededException() {
        super("Too many failed attempts. Request a new verification code.");
    }
}
