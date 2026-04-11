package com.xeubiart.account.exceptions;

import com.xeubiart.infra.exceptions.ApplicationException;

public class AccountInvalidCredentialsException extends ApplicationException {
    public AccountInvalidCredentialsException(String message) {
        super(message, "INVALID_CREDENTIALS");
    }
}
