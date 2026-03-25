package com.recruitment.skybook.dto.booking;

import lombok.*;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class BookingRequest {
    private Long flightId;
    private List<PassengerDto> passengers;
    private PaymentDto payment;
}
