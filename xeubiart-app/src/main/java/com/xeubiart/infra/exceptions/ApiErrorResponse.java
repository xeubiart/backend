package com.xeubiart.infra.exceptions;

import com.xeubiart.core.exceptions.ApplicationException;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Data
@Builder
public class ApiErrorResponse {
    private int status;
    private String code;
    private String message;
    private LocalDateTime timestamp;

    public static ResponseEntity<ApiErrorResponse> fromException(ApplicationException exception){
        return ResponseEntity.status(exception.getHttpCode())
                .body(ApiErrorResponse.builder()
                        .status(exception.getHttpCode())
                        .code(exception.getCode())
                        .message(exception.getMessage())
                        .timestamp(LocalDateTime.now())
                        .build());
    }
}
