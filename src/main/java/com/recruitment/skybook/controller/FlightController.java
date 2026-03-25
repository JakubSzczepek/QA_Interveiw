package com.recruitment.skybook.controller;

import com.recruitment.skybook.dto.flight.FlightRequest;
import com.recruitment.skybook.dto.flight.FlightResponse;
import com.recruitment.skybook.service.FlightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/flights")
@RequiredArgsConstructor
@Tag(name = "Flights", description = "Flight management endpoints. Business rules: BR-01 to BR-10")
public class FlightController {

    private final FlightService flightService;

    @GetMapping
    @Operation(summary = "List all flights", description = "Returns paginated list of flights. Default: page=0, size=10, max size=100 (BR-18)")
    public ResponseEntity<Page<FlightResponse>> getAllFlights(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(flightService.getAllFlights(page, size));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get flight by ID", description = "Returns flight details with nested segments, pricing (taxes, fees, discount), and available seats")
    public ResponseEntity<FlightResponse> getFlightById(@PathVariable Long id) {
        return ResponseEntity.ok(flightService.getFlightById(id));
    }

    // BUG-09: Returns 200 OK instead of 201 Created
    @PostMapping
    @Operation(summary = "Create a new flight", description = "Creates flight with segments, pricing. flightNumber must be unique (BR-01). Segments required (BR-02). Returns the created flight.")
    public ResponseEntity<FlightResponse> createFlight(@RequestBody FlightRequest request) {
        FlightResponse response = flightService.createFlight(request);
        // BUG-09: Should be ResponseEntity.status(HttpStatus.CREATED).body(response)
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a flight", description = "Full update of flight data. Validates all business rules.")
    public ResponseEntity<FlightResponse> updateFlight(@PathVariable Long id, @RequestBody FlightRequest request) {
        return ResponseEntity.ok(flightService.updateFlight(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a flight", description = "Removes a flight. Should fail if flight has associated bookings (BR-16).")
    public ResponseEntity<Void> deleteFlight(@PathVariable Long id) {
        flightService.deleteFlight(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search flights", description = "Search by origin, destination, date. Case-insensitive, partial match (BR-19). Paginated.")
    public ResponseEntity<Page<FlightResponse>> searchFlights(
            @RequestParam(required = false) String origin,
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) String date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(flightService.searchFlights(origin, destination, date, page, size));
    }
}
