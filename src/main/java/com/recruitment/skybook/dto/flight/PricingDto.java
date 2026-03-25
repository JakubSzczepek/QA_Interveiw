package com.recruitment.skybook.dto.flight;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class PricingDto {
    private String currency;
    private BigDecimal baseFare;
    private List<TaxDto> taxes;
    private List<FeeDto> fees;
    private BigDecimal totalAmount;
    private DiscountDto discount;
}
