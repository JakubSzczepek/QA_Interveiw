package com.recruitment.skybook.model;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "passengers")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Passenger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int sequenceNumber;

    // Personal info
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String nationality;

    // Contact
    private String email;
    private String phone;

    // Seat assignment (optional)
    private String seatNumber;
    private String seatClass;
    private String seatType;

    // Special requests
    @ElementCollection
    @CollectionTable(name = "passenger_special_requests", joinColumns = @JoinColumn(name = "passenger_id"))
    @Column(name = "request")
    @Builder.Default
    private List<String> specialRequests = new ArrayList<>();

    // Baggage
    @OneToMany(mappedBy = "passenger", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<BaggageItem> baggageItems = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    @JsonIgnore
    private Booking booking;
}
