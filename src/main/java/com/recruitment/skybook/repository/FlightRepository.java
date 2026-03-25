package com.recruitment.skybook.repository;

import com.recruitment.skybook.model.Flight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    Optional<Flight> findByFlightNumber(String flightNumber);

    boolean existsByFlightNumber(String flightNumber);

    // BUG-10: Case-sensitive search — uses LIKE without LOWER(), so "warsaw" won't match "Warsaw"
    @Query("SELECT f FROM Flight f JOIN f.segments s WHERE " +
           "(:origin IS NULL OR s.departureAirportCode LIKE %:origin% OR s.departureAirportName LIKE %:origin%) AND " +
           "(:destination IS NULL OR s.arrivalAirportCode LIKE %:destination% OR s.arrivalAirportName LIKE %:destination%) AND " +
           "(:date IS NULL OR s.departureDateTime LIKE %:date%)")
    Page<Flight> searchFlights(@Param("origin") String origin,
                               @Param("destination") String destination,
                               @Param("date") String date,
                               Pageable pageable);
}
