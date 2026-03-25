package com.recruitment.skybook.controller;

import com.recruitment.skybook.dto.booking.BookingRequest;
import com.recruitment.skybook.dto.booking.BookingResponse;
import com.recruitment.skybook.dto.booking.BookingStatusRequest;
import com.recruitment.skybook.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
@Tag(name = "Bookings", description = "Booking management endpoints. Business rules: BR-11 to BR-15")
public class BookingController {

    private final BookingService bookingService;

    @GetMapping
    @Operation(summary = "List all bookings", description = "Returns paginated list of bookings with passengers, payment, and pricing summary.")
    public ResponseEntity<Page<BookingResponse>> getAllBookings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(bookingService.getAllBookings(page, size));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get booking by ID", description = "Returns booking with nested passengers (personalInfo, contact, seatAssignment, baggage), payment (cardDetails), and pricingSummary.")
    public ResponseEntity<BookingResponse> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    // BUG-09: Returns 200 OK instead of 201 Created
    @PostMapping
    @Operation(summary = "Create a booking", description = "Creates booking for a flight. Requires passengers[] (BR-11), payment (BR-14). Seat assignment is optional (BR-13).")
    public ResponseEntity<BookingResponse> createBooking(@RequestBody BookingRequest request) {
        BookingResponse response = bookingService.createBooking(request);
        // BUG-09: Should be ResponseEntity.status(HttpStatus.CREATED).body(response)
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update booking status", description = "Allowed transitions: PENDING→CONFIRMED, PENDING→CANCELLED, CONFIRMED→CANCELLED. Cannot revert CANCELLED (BR-15).")
    public ResponseEntity<BookingResponse> updateBookingStatus(
            @PathVariable Long id,
            @Valid @RequestBody BookingStatusRequest request) {
        return ResponseEntity.ok(bookingService.updateBookingStatus(id, request));
    }
}
