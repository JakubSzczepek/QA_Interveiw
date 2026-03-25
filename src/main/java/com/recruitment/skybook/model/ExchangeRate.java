package com.recruitment.skybook.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "exchange_rates")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ExchangeRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String baseCurrency;
    private String targetCurrency;
    private double rate;
    private LocalDateTime timestamp;
}
