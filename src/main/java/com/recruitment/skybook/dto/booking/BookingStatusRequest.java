package com.recruitment.skybook.dto.booking;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class BookingStatusRequest {
    private String status;
}
