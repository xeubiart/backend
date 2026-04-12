package com.xeubiart.verification.exceptions;

import com.xeubiart.core.exceptions.ApplicationException;

public class VerificationReSendCooldownException extends ApplicationException {
    public VerificationReSendCooldownException() {
        super("Please wait before resending the verification code.", "ERR_VERIFICATION_RESEND_COOLDOWN", 425);
    }
}
