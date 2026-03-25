package com.recruitment.skybook.dto.flight;

import lombok.*;
import java.math.BigDecimal;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class TaxDto {
    private String code;
    private String name;
    private BigDecimal amount;
}
