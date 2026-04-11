package com.xeubiart.infra.exceptions;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ApiErrorResponse {
    private int status;
    private String code;
    private String message;
    private LocalDateTime timestamp;
}
