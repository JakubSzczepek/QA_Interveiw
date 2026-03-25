package com.recruitment.skybook.dto.flight;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class DiscountDto {
    @NotBlank(message = "Discount code is required")
    private String code;

    // BUG-06: Intentionally no @Max(100) — values > 100 accepted
    @NotNull(message = "Discount percentage is required")
    private Double percentage;

    private String validUntil;
}
