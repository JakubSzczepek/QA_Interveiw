package com.recruitment.skybook.dto.booking;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class BookingStatusRequest {
    @NotBlank(message = "Status is required")
    private String status;
}
