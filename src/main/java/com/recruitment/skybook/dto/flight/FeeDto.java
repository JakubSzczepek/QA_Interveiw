package com.recruitment.skybook.dto.flight;

import lombok.*;
import java.math.BigDecimal;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class FeeDto {
    private String feeCode;
    private String description;
    private BigDecimal amount;
    private boolean optional;
}
