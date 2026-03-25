package com.recruitment.skybook.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bookings")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String bookingReference;

    private Long flightId;

    private String status; // PENDING, CONFIRMED, CANCELLED

    // Payment (flattened)
    private String paymentMethod;
    private String paymentStatus;
    private BigDecimal paymentAmount;
    private String paymentCurrency;
    private String cardLastFour;
    private String cardBrand;
    private String cardHolderName;

    // Pricing summary (flattened)
    private String pricingSummaryCurrency;
    private BigDecimal pricingSummaryBaseFare;
    private BigDecimal pricingSummaryTotalTaxes;
    private BigDecimal pricingSummaryTotalFees;
    private BigDecimal pricingSummaryDiscountAmount;
    private BigDecimal pricingSummaryGrandTotal;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sequenceNumber ASC")
    @Builder.Default
    private List<Passenger> passengers = new ArrayList<>();

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
