package com.recruitment.skybook.dto.booking;

import lombok.*;
import java.math.BigDecimal;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class PaymentResponseDto {
    private String method;
    private String status;
    private BigDecimal amount;
    private String currency;
    private CardDetailsDto cardDetails;
}
