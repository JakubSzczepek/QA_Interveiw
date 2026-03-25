package com.recruitment.skybook.dto.booking;

import lombok.*;
import java.math.BigDecimal;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class PricingSummaryDto {
    private String currency;
    private BigDecimal baseFare;
    private BigDecimal totalTaxes;
    private BigDecimal totalFees;
    private BigDecimal discountAmount;
    private BigDecimal grandTotal;
}
