package com.recruitment.skybook.dto.airport;

import lombok.*;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class AirportResponse {
    private String code;
    private String name;
    private String city;
    private String country;
    private String timezone;
    private CoordinatesDto coordinates;
    private List<TerminalDto> terminals;
}
