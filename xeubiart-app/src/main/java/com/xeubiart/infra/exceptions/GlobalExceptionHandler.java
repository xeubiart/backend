package com.xeubiart.infra.exceptions;

import com.xeubiart.account.exceptions.AccountInvalidCredentialsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    // Application level exceptions

    @ExceptionHandler(AccountInvalidCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDenied(ApplicationException ex) {
        log.warn("Access denied: {}", ex.getMessage());

        return this.buildResponse(HttpStatus.FORBIDDEN, ex.getCode(), "Trying to login with invalid credentials.");
    }

    // Spring level exceptions

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleBadRequest(AuthorizationDeniedException ex){
        return this.buildResponse(HttpStatus.FORBIDDEN, "ERR_AUTHENTICATION_REQUIRED", "You are not authorized to perform this action.");
    }

    // ====================================== //

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericError(Exception ex){
        log.error("Unhandled exception caught in GlobalExceptionHandler: {}", ex.getMessage(), ex);

        return this.buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "ERR_INTERNAL_SERVER", "Something went wrong. Please try again later.");
    }

    private ResponseEntity<ApiErrorResponse> buildResponseFromException(HttpStatus status, ApplicationException ex) {
        ApiErrorResponse error = ApiErrorResponse.builder()
                .status(status.value())
                .code(ex.getCode())
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(status).body(error);
    }

    private ResponseEntity<ApiErrorResponse> buildResponse(HttpStatus status, String code, String message) {
        ApiErrorResponse error = ApiErrorResponse.builder()
                .status(status.value())
                .code(code)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(status).body(error);
    }
}