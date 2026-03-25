package com.recruitment.skybook.service;

import com.recruitment.skybook.dto.pricing.CurrencyConversionResponse;
import com.recruitment.skybook.dto.pricing.ExchangeRatesResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class PricingService {

    // Exchange rates with EUR as base currency
    // BUG-07: EUR→GBP intentionally uses rate 1.0 instead of correct 0.8614
    private static final Map<String, Double> EUR_RATES = new LinkedHashMap<>();
    private static final String RATE_TIMESTAMP = "2026-03-25T10:00:00";

    static {
        EUR_RATES.put("USD", 1.0842);
        EUR_RATES.put("GBP", 1.0);    // BUG-07: Should be 0.8614, but is 1.0
        EUR_RATES.put("PLN", 4.2850);
        EUR_RATES.put("EUR", 1.0);
    }

    public CurrencyConversionResponse convert(BigDecimal amount, String from, String to) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount must be >= 0");
        }
        if (!EUR_RATES.containsKey(from)) {
            throw new IllegalArgumentException("Unsupported currency: " + from);
        }
        if (!EUR_RATES.containsKey(to)) {
            throw new IllegalArgumentException("Unsupported currency: " + to);
        }

        // Convert: from → EUR → to
        double fromRate = EUR_RATES.get(from);
        double toRate = EUR_RATES.get(to);
        double exchangeRate = toRate / fromRate;

        BigDecimal converted = amount.multiply(BigDecimal.valueOf(exchangeRate))
                .setScale(2, RoundingMode.HALF_UP);

        return CurrencyConversionResponse.builder()
                .originalAmount(amount)
                .originalCurrency(from)
                .convertedAmount(converted)
                .targetCurrency(to)
                .exchangeRate(Math.round(exchangeRate * 10000.0) / 10000.0)
                .rateTimestamp(RATE_TIMESTAMP)
                .build();
    }

    public ExchangeRatesResponse getRates() {
        Map<String, Double> rates = new LinkedHashMap<>();
        rates.put("USD", EUR_RATES.get("USD"));
        rates.put("GBP", EUR_RATES.get("GBP")); // BUG-07: returns 1.0 instead of 0.8614
        rates.put("PLN", EUR_RATES.get("PLN"));

        return ExchangeRatesResponse.builder()
                .baseCurrency("EUR")
                .rates(rates)
                .timestamp(RATE_TIMESTAMP)
                .build();
    }
}
