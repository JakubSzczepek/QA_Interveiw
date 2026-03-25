package com.recruitment.skybook.model;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "baggage_items")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BaggageItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type; // CHECKED, CABIN
    private int weightKg;
    private int count;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "passenger_id")
    @JsonIgnore
    private Passenger passenger;
}
