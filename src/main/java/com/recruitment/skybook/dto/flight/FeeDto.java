package com.recruitment.skybook.dto.flight;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class FeeDto {
    @NotBlank(message = "Fee code is required")
    private String feeCode;

    private String description;

    @NotNull(message = "Fee amount is required")
    @DecimalMin(value = "0.00", message = "Fee amount must be >= 0 (BR-07)")
    private BigDecimal amount;

    private boolean optional;
}
