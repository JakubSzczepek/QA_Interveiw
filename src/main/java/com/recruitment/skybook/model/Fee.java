package com.recruitment.skybook.model;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;

@Entity
@Table(name = "fees")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Fee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String feeCode;
    private String description;
    private BigDecimal amount;
    private boolean optional;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id")
    @JsonIgnore
    private Flight flight;
}
