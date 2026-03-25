package com.recruitment.skybook.dto.flight;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class DepartureArrivalDto {
    private String airportCode;
    private String airportName;
    private String terminal;
    private String dateTime;
}
