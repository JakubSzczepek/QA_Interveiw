package com.recruitment.skybook.dto.flight;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import java.math.BigDecimal;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class TaxDto {
    @NotBlank(message = "Tax code is required")
    @Pattern(regexp = "^[A-Z]{2}$", message = "Tax code must be 2 uppercase letters (BR-06)")
    private String code;

    private String name;

    // BUG-05: Intentionally no @PositiveOrZero — negative amounts accepted
    @NotNull(message = "Tax amount is required")
    private BigDecimal amount;
}
