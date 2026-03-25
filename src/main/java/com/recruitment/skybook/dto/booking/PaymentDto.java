package com.recruitment.skybook.dto.booking;

import lombok.*;
import java.math.BigDecimal;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class PaymentDto {
    private String method;
    private String currency;
    private CardDetailsDto cardDetails;
}
