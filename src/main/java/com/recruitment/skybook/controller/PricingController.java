package com.recruitment.skybook.controller;

import com.recruitment.skybook.dto.pricing.CurrencyConversionResponse;
import com.recruitment.skybook.dto.pricing.ExchangeRatesResponse;
import com.recruitment.skybook.service.PricingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/pricing")
@RequiredArgsConstructor
@Tag(name = "Pricing", description = "Currency conversion and exchange rates. Supported currencies: USD, EUR, GBP, PLN (BR-17)")
public class PricingController {

    private final PricingService pricingService;

    @GetMapping("/convert")
    @Operation(summary = "Convert currency", description = "Converts amount between supported currencies (USD, EUR, GBP, PLN). Uses EUR as base rate.")
    public ResponseEntity<CurrencyConversionResponse> convertCurrency(
            @RequestParam BigDecimal amount,
            @RequestParam String from,
            @RequestParam String to) {
        return ResponseEntity.ok(pricingService.convert(amount, from.toUpperCase(), to.toUpperCase()));
    }

    @GetMapping("/rates")
    @Operation(summary = "Get exchange rates", description = "Returns current exchange rates with EUR as base currency.")
    public ResponseEntity<ExchangeRatesResponse> getExchangeRates() {
        return ResponseEntity.ok(pricingService.getRates());
    }
}
