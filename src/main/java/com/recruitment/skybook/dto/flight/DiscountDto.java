package com.recruitment.skybook.dto.flight;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class DiscountDto {
    private String code;
    private Double percentage;
    private String validUntil;
}
