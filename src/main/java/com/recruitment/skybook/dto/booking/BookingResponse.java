package com.recruitment.skybook.dto.booking;

import lombok.*;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class BookingResponse {
    private Long id;
    private String bookingReference;
    private Long flightId;
    private String status;
    private List<PassengerResponseDto> passengers;
    private PaymentResponseDto payment;
    private PricingSummaryDto pricingSummary;
    private String createdAt;
    private String updatedAt;
}
