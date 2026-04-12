package com.xeubiart.account.exceptions;

import com.xeubiart.core.exceptions.ApplicationException;

public class AccountNotFoundException extends ApplicationException {
    public AccountNotFoundException(String message) {
        super(message, "ERR_ACCOUNT_NOT_FOUND", 404);
    }
}
