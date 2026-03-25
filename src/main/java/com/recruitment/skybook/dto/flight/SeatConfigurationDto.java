package com.recruitment.skybook.dto.flight;

import jakarta.validation.constraints.Min;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class SeatConfigurationDto {
    @Min(value = 0, message = "Economy seats must be >= 0 (BR-04)")
    private int economy;

    @Min(value = 0, message = "Business seats must be >= 0 (BR-04)")
    private int business;

    @Min(value = 0, message = "First class seats must be >= 0 (BR-04)")
    private int first;
}
