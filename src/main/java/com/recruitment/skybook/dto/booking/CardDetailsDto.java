package com.recruitment.skybook.dto.booking;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class CardDetailsDto {
    private String lastFourDigits;
    private String brand;
    private String holderName;
}
