package com.recruitment.skybook.repository;

import com.recruitment.skybook.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByFlightId(Long flightId);

    boolean existsByFlightId(Long flightId);
}
