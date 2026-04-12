package com.xeubiart.core.exceptions;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException{
    private final String code;
    private final int httpCode;
    public ApplicationException(String message, String code, int httpStatus) {
        super(message);
        this.code = code;
        this.httpCode = httpStatus;
    }
}
