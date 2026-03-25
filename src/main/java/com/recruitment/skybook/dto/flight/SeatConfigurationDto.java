package com.recruitment.skybook.dto.flight;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class SeatConfigurationDto {
    private int economy;
    private int business;
    private int first;
}
