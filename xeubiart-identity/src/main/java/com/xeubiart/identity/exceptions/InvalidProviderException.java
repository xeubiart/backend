package com.xeubiart.identity.exceptions;

import com.xeubiart.core.exceptions.ApplicationException;

public class InvalidProviderException extends ApplicationException {
    public InvalidProviderException(String providerName) {
        super("Invalid provider: " + providerName, "ERR_INVALID_PROVIDER", 400);
    }
}
