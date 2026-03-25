package com.recruitment.skybook.dto.booking;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class SeatAssignmentDto {
    private String seatNumber;
    private String seatClass; // renamed to avoid Java keyword conflict

    // Use custom getter/setter for JSON field name "class"
    @com.fasterxml.jackson.annotation.JsonProperty("class")
    public String getSeatClass() {
        return seatClass;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("class")
    public void setSeatClass(String seatClass) {
        this.seatClass = seatClass;
    }

    private String type;
}
