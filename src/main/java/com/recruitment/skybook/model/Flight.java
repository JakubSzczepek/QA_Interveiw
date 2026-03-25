package com.recruitment.skybook.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "flights")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String flightNumber;

    private String airline;

    private String status; // SCHEDULED, DELAYED, CANCELLED, COMPLETED

    // --- Pricing (flattened) ---
    private String pricingCurrency;
    private BigDecimal pricingBaseFare;
    private BigDecimal pricingTotalAmount;

    // Discount (optional, flattened)
    private String discountCode;
    private Double discountPercentage;
    private String discountValidUntil; // stored as string for simplicity

    // --- Available seats (flattened) ---
    private int availableSeatsEconomy;
    private int availableSeatsBusiness;
    private int availableSeatsFirst;

    // --- Segments ---
    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("segmentNumber ASC")
    @Builder.Default
    private List<Segment> segments = new ArrayList<>();

    // --- Taxes ---
    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Tax> taxes = new ArrayList<>();

    // --- Fees ---
    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Fee> fees = new ArrayList<>();

    // --- Tags ---
    @ElementCollection
    @CollectionTable(name = "flight_tags", joinColumns = @JoinColumn(name = "flight_id"))
    @Column(name = "tag")
    @Builder.Default
    private List<String> tags = new ArrayList<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
