package com.recruitment.skybook.dto.pricing;

import lombok.*;
import java.util.Map;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ExchangeRatesResponse {
    private String baseCurrency;
    private Map<String, Double> rates;
    private String timestamp;
}
