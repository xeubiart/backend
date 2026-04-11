package com.xeubiart.verification.exceptions;

public class VerificationReSendCooldownException extends VerificationException {
    public VerificationReSendCooldownException() {
        super("Please wait before resending the verification code.");
    }
}
