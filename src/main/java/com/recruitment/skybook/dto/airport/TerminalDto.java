package com.recruitment.skybook.dto.airport;

import lombok.*;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class TerminalDto {
    private String name;
    private List<String> gates;
    private List<String> facilities;
}
