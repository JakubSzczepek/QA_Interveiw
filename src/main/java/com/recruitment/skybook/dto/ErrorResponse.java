package com.recruitment.skybook.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ErrorResponse {
    private int status;
    private String error;
    private String message;
    private String timestamp;
    private String path;
}
