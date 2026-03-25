package com.recruitment.skybook.controller;

import com.recruitment.skybook.dto.airport.AirportResponse;
import com.recruitment.skybook.service.AirportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/airports")
@RequiredArgsConstructor
@Tag(name = "Airports", description = "Airport reference data (read-only, pre-seeded)")
public class AirportController {

    private final AirportService airportService;

    @GetMapping
    @Operation(summary = "List all airports", description = "Returns all pre-loaded airports with terminals, gates, and coordinates.")
    public ResponseEntity<List<AirportResponse>> getAllAirports() {
        return ResponseEntity.ok(airportService.getAllAirports());
    }

    @GetMapping("/{code}")
    @Operation(summary = "Get airport by IATA code", description = "Returns airport details by its 3-letter IATA code (e.g., WAW, FRA, LHR).")
    public ResponseEntity<AirportResponse> getAirportByCode(@PathVariable String code) {
        return ResponseEntity.ok(airportService.getAirportByCode(code));
    }
}
