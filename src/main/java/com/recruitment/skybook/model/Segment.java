package com.recruitment.skybook.model;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "segments")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Segment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int segmentNumber;

    // Departure
    private String departureAirportCode;
    private String departureAirportName;
    private String departureTerminal;
    private String departureDateTime;

    // Arrival
    private String arrivalAirportCode;
    private String arrivalAirportName;
    private String arrivalTerminal;
    private String arrivalDateTime;

    // Aircraft
    private String aircraftModel;
    private String aircraftRegistration;
    private int seatConfigEconomy;
    private int seatConfigBusiness;
    private int seatConfigFirst;

    private int durationMinutes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id")
    @JsonIgnore
    private Flight flight;
}
