package com.recruitment.skybook.dto.flight;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class SegmentDto {
    private int segmentNumber;
    private DepartureArrivalDto departure;
    private DepartureArrivalDto arrival;
    private AircraftDto aircraft;
    private int durationMinutes;
}
