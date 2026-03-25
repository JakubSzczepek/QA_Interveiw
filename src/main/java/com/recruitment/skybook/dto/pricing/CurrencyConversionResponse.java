package com.recruitment.skybook.dto.pricing;

import lombok.*;
import java.math.BigDecimal;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class CurrencyConversionResponse {
    private BigDecimal originalAmount;
    private String originalCurrency;
    private BigDecimal convertedAmount;
    private String targetCurrency;
    private double exchangeRate;
    private String rateTimestamp;
}
