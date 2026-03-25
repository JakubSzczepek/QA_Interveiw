package com.recruitment.skybook.dto.booking;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class BaggageDto {
    private String type;
    private int weightKg;
    private int count;
}
