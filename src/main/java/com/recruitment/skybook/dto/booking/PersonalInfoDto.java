package com.recruitment.skybook.dto.booking;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class PersonalInfoDto {
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String nationality;
}
