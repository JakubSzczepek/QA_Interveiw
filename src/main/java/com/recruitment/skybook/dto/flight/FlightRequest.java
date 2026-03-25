package com.recruitment.skybook.dto.flight;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class FlightRequest {
    private String flightNumber;
    private String airline;
    private String status;
    private List<SegmentRequestDto> segments;
    private PricingRequestDto pricing;
    private List<String> tags;

    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class SegmentRequestDto {
        private int segmentNumber;
        private DepartureArrivalRequestDto departure;
        private DepartureArrivalRequestDto arrival;
        private AircraftRequestDto aircraft;
        private int durationMinutes;
    }

    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class DepartureArrivalRequestDto {
        private String airportCode;
        private String terminal;
        private String dateTime;
    }

    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class AircraftRequestDto {
        private String model;
        private String registration;
        private SeatConfigurationDto seatConfiguration;
    }

    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class PricingRequestDto {
        private String currency;
        private BigDecimal baseFare;
        private List<TaxDto> taxes;
        private List<FeeDto> fees;
        private DiscountDto discount;
    }
}
