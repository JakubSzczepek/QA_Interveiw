package com.recruitment.skybook.config;

import com.recruitment.skybook.model.*;
import com.recruitment.skybook.repository.AirportRepository;
import com.recruitment.skybook.repository.BookingRepository;
import com.recruitment.skybook.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final AirportRepository airportRepository;
    private final FlightRepository flightRepository;
    private final BookingRepository bookingRepository;

    @Override
    public void run(String... args) {
        seedAirports();
        seedFlights();
        seedBookings();
        log.info("=== SkyBook API — Seed data loaded ===");
        log.info("Airports: {}", airportRepository.count());
        log.info("Flights: {}", flightRepository.count());
        log.info("Bookings: {}", bookingRepository.count());
    }

    private void seedAirports() {
        // WAW
        Airport waw = Airport.builder()
                .code("WAW").name("Warsaw Chopin Airport").city("Warsaw").country("PL")
                .timezone("Europe/Warsaw").latitude(52.1657).longitude(20.9671).build();
        Terminal wawA = Terminal.builder().name("A")
                .gates(List.of("A1", "A2", "A3", "A4"))
                .facilities(List.of("LOUNGE", "DUTY_FREE", "RESTAURANT"))
                .airport(waw).build();
        waw.setTerminals(new ArrayList<>(List.of(wawA)));

        // FRA
        Airport fra = Airport.builder()
                .code("FRA").name("Frankfurt Airport").city("Frankfurt").country("DE")
                .timezone("Europe/Berlin").latitude(50.0379).longitude(8.5622).build();
        Terminal fra1 = Terminal.builder().name("1")
                .gates(List.of("B1", "B2", "B3", "B4", "B5"))
                .facilities(List.of("LOUNGE", "DUTY_FREE", "RESTAURANT", "HOTEL"))
                .airport(fra).build();
        Terminal fra2 = Terminal.builder().name("2")
                .gates(List.of("D1", "D2", "D3"))
                .facilities(List.of("DUTY_FREE", "RESTAURANT"))
                .airport(fra).build();
        fra.setTerminals(new ArrayList<>(List.of(fra1, fra2)));

        // LHR
        Airport lhr = Airport.builder()
                .code("LHR").name("London Heathrow Airport").city("London").country("GB")
                .timezone("Europe/London").latitude(51.4700).longitude(-0.4543).build();
        Terminal lhr5 = Terminal.builder().name("5")
                .gates(List.of("A1", "A2", "A3", "A4", "A5", "A6"))
                .facilities(List.of("LOUNGE", "DUTY_FREE", "RESTAURANT", "SPA"))
                .airport(lhr).build();
        lhr.setTerminals(new ArrayList<>(List.of(lhr5)));

        // JFK
        Airport jfk = Airport.builder()
                .code("JFK").name("John F. Kennedy International Airport").city("New York").country("US")
                .timezone("America/New_York").latitude(40.6413).longitude(-73.7781).build();
        Terminal jfk1 = Terminal.builder().name("1")
                .gates(List.of("1", "2", "3", "4"))
                .facilities(List.of("LOUNGE", "DUTY_FREE"))
                .airport(jfk).build();
        jfk.setTerminals(new ArrayList<>(List.of(jfk1)));

        // CDG
        Airport cdg = Airport.builder()
                .code("CDG").name("Charles de Gaulle Airport").city("Paris").country("FR")
                .timezone("Europe/Paris").latitude(49.0097).longitude(2.5479).build();
        Terminal cdg2 = Terminal.builder().name("2E")
                .gates(List.of("K1", "K2", "K3", "K4", "K5"))
                .facilities(List.of("LOUNGE", "DUTY_FREE", "RESTAURANT"))
                .airport(cdg).build();
        cdg.setTerminals(new ArrayList<>(List.of(cdg2)));

        // BCN
        Airport bcn = Airport.builder()
                .code("BCN").name("Barcelona-El Prat Airport").city("Barcelona").country("ES")
                .timezone("Europe/Madrid").latitude(41.2974).longitude(2.0833).build();
        Terminal bcnT1 = Terminal.builder().name("T1")
                .gates(List.of("A1", "A2", "A3", "B1", "B2"))
                .facilities(List.of("LOUNGE", "DUTY_FREE", "RESTAURANT"))
                .airport(bcn).build();
        bcn.setTerminals(new ArrayList<>(List.of(bcnT1)));

        airportRepository.saveAll(List.of(waw, fra, lhr, jfk, cdg, bcn));
    }

    private void seedFlights() {
        // Flight 1: WAW → FRA (direct, morning, ECONOMY+BUSINESS)
        Flight f1 = Flight.builder()
                .flightNumber("SB-1234").airline("SkyBook Airlines").status("SCHEDULED")
                .pricingCurrency("EUR").pricingBaseFare(new BigDecimal("120.00"))
                .discountCode("EARLY_BIRD").discountPercentage(10.0).discountValidUntil("2026-05-01")
                .availableSeatsEconomy(84).availableSeatsBusiness(12).availableSeatsFirst(0)
                .tags(new ArrayList<>(List.of("direct", "morning", "short-haul")))
                .build();
        Segment f1s1 = Segment.builder()
                .segmentNumber(1)
                .departureAirportCode("WAW").departureAirportName("Warsaw Chopin Airport").departureTerminal("A").departureDateTime("2026-06-15T08:30:00")
                .arrivalAirportCode("FRA").arrivalAirportName("Frankfurt Airport").arrivalTerminal("1").arrivalDateTime("2026-06-15T10:45:00")
                .aircraftModel("Boeing 737-800").aircraftRegistration("SP-LKA")
                .seatConfigEconomy(162).seatConfigBusiness(18).seatConfigFirst(0)
                .durationMinutes(135).flight(f1).build();
        f1.setSegments(new ArrayList<>(List.of(f1s1)));
        Tax f1t1 = Tax.builder().code("YQ").name("Fuel Surcharge").amount(new BigDecimal("45.00")).flight(f1).build();
        Tax f1t2 = Tax.builder().code("PL").name("Passenger Service Charge").amount(new BigDecimal("12.50")).flight(f1).build();
        Tax f1t3 = Tax.builder().code("DE").name("Airport Tax").amount(new BigDecimal("8.00")).flight(f1).build();
        f1.setTaxes(new ArrayList<>(List.of(f1t1, f1t2, f1t3)));
        Fee f1fee1 = Fee.builder().feeCode("BAGGAGE").description("Checked baggage 23kg").amount(new BigDecimal("25.00")).optional(true).flight(f1).build();
        Fee f1fee2 = Fee.builder().feeCode("SEAT_SELECTION").description("Seat selection fee").amount(new BigDecimal("5.00")).optional(true).flight(f1).build();
        f1.setFees(new ArrayList<>(List.of(f1fee1, f1fee2)));
        // BUG-08: totalAmount should be 120 + 65.5 + 30 - 21.55 = 193.95 but since fees are excluded: 120 + 65.5 - 18.55 = 166.95
        f1.setPricingTotalAmount(new BigDecimal("166.95"));

        // Flight 2: LHR → JFK (direct, BUSINESS class, afternoon)
        Flight f2 = Flight.builder()
                .flightNumber("SB-5678").airline("SkyBook Airlines").status("SCHEDULED")
                .pricingCurrency("GBP").pricingBaseFare(new BigDecimal("850.00"))
                .availableSeatsEconomy(0).availableSeatsBusiness(42).availableSeatsFirst(8)
                .tags(new ArrayList<>(List.of("direct", "afternoon", "long-haul", "business")))
                .build();
        Segment f2s1 = Segment.builder()
                .segmentNumber(1)
                .departureAirportCode("LHR").departureAirportName("London Heathrow Airport").departureTerminal("5").departureDateTime("2026-07-01T14:00:00")
                .arrivalAirportCode("JFK").arrivalAirportName("John F. Kennedy International Airport").arrivalTerminal("1").arrivalDateTime("2026-07-01T17:30:00")
                .aircraftModel("Boeing 777-300ER").aircraftRegistration("G-SKBA")
                .seatConfigEconomy(0).seatConfigBusiness(42).seatConfigFirst(8)
                .durationMinutes(450).flight(f2).build();
        f2.setSegments(new ArrayList<>(List.of(f2s1)));
        Tax f2t1 = Tax.builder().code("GB").name("UK Air Passenger Duty").amount(new BigDecimal("78.00")).flight(f2).build();
        Tax f2t2 = Tax.builder().code("YQ").name("Fuel Surcharge").amount(new BigDecimal("120.00")).flight(f2).build();
        f2.setTaxes(new ArrayList<>(List.of(f2t1, f2t2)));
        Fee f2fee1 = Fee.builder().feeCode("PRIORITY_BOARDING").description("Priority boarding").amount(new BigDecimal("15.00")).optional(true).flight(f2).build();
        f2.setFees(new ArrayList<>(List.of(f2fee1)));
        // BUG-08: should be 850 + 198 + 15 = 1063, but fees excluded: 850 + 198 = 1048
        f2.setPricingTotalAmount(new BigDecimal("1048.00"));

        // Flight 3: CDG → BCN (direct, budget, evening)
        Flight f3 = Flight.builder()
                .flightNumber("SB-9012").airline("SkyBook Airlines").status("SCHEDULED")
                .pricingCurrency("EUR").pricingBaseFare(new BigDecimal("45.00"))
                .discountCode("SUMMER23").discountPercentage(15.0).discountValidUntil("2026-08-31")
                .availableSeatsEconomy(189).availableSeatsBusiness(0).availableSeatsFirst(0)
                .tags(new ArrayList<>(List.of("direct", "evening", "short-haul", "budget")))
                .build();
        Segment f3s1 = Segment.builder()
                .segmentNumber(1)
                .departureAirportCode("CDG").departureAirportName("Charles de Gaulle Airport").departureTerminal("2E").departureDateTime("2026-08-10T19:00:00")
                .arrivalAirportCode("BCN").arrivalAirportName("Barcelona-El Prat Airport").arrivalTerminal("T1").arrivalDateTime("2026-08-10T21:00:00")
                .aircraftModel("Airbus A320neo").aircraftRegistration("F-SKBC")
                .seatConfigEconomy(189).seatConfigBusiness(0).seatConfigFirst(0)
                .durationMinutes(120).flight(f3).build();
        f3.setSegments(new ArrayList<>(List.of(f3s1)));
        Tax f3t1 = Tax.builder().code("FR").name("French Aviation Tax").amount(new BigDecimal("7.50")).flight(f3).build();
        f3.setTaxes(new ArrayList<>(List.of(f3t1)));
        // No fees, no discount applied to totalAmount:
        // BUG-08: 45 + 7.5 - (15% of 52.5 = 7.875 ≈ 7.88) = 44.62
        f3.setPricingTotalAmount(new BigDecimal("44.63"));

        // Flight 4: WAW → LHR via FRA (connection, 2 segments)
        Flight f4 = Flight.builder()
                .flightNumber("SB-3456").airline("SkyBook Airlines").status("SCHEDULED")
                .pricingCurrency("PLN").pricingBaseFare(new BigDecimal("520.00"))
                .availableSeatsEconomy(150).availableSeatsBusiness(16).availableSeatsFirst(0)
                .tags(new ArrayList<>(List.of("connection", "morning", "medium-haul")))
                .build();
        Segment f4s1 = Segment.builder()
                .segmentNumber(1)
                .departureAirportCode("WAW").departureAirportName("Warsaw Chopin Airport").departureTerminal("A").departureDateTime("2026-09-05T06:00:00")
                .arrivalAirportCode("FRA").arrivalAirportName("Frankfurt Airport").arrivalTerminal("1").arrivalDateTime("2026-09-05T08:15:00")
                .aircraftModel("Embraer E195").aircraftRegistration("SP-LKD")
                .seatConfigEconomy(150).seatConfigBusiness(16).seatConfigFirst(0)
                .durationMinutes(135).flight(f4).build();
        Segment f4s2 = Segment.builder()
                .segmentNumber(2)
                .departureAirportCode("FRA").departureAirportName("Frankfurt Airport").departureTerminal("2").departureDateTime("2026-09-05T10:00:00")
                .arrivalAirportCode("LHR").arrivalAirportName("London Heathrow Airport").arrivalTerminal("5").arrivalDateTime("2026-09-05T10:45:00")
                .aircraftModel("Airbus A319").aircraftRegistration("D-SKBE")
                .seatConfigEconomy(138).seatConfigBusiness(12).seatConfigFirst(0)
                .durationMinutes(105).flight(f4).build();
        f4.setSegments(new ArrayList<>(List.of(f4s1, f4s2)));
        Tax f4t1 = Tax.builder().code("PL").name("Polish Aviation Tax").amount(new BigDecimal("35.00")).flight(f4).build();
        Tax f4t2 = Tax.builder().code("DE").name("German Transit Tax").amount(new BigDecimal("15.00")).flight(f4).build();
        Tax f4t3 = Tax.builder().code("GB").name("UK APD").amount(new BigDecimal("52.00")).flight(f4).build();
        f4.setTaxes(new ArrayList<>(List.of(f4t1, f4t2, f4t3)));
        Fee f4fee1 = Fee.builder().feeCode("BAGGAGE").description("Checked baggage 23kg").amount(new BigDecimal("80.00")).optional(true).flight(f4).build();
        Fee f4fee2 = Fee.builder().feeCode("INSURANCE").description("Travel insurance").amount(new BigDecimal("45.00")).optional(true).flight(f4).build();
        f4.setFees(new ArrayList<>(List.of(f4fee1, f4fee2)));
        // BUG-08: should be 520+102+125 = 747, bug: 520+102 = 622
        f4.setPricingTotalAmount(new BigDecimal("622.00"));

        // Flight 5: JFK → CDG (direct, USD pricing, delayed)
        Flight f5 = Flight.builder()
                .flightNumber("SB-7890").airline("SkyBook Airlines").status("DELAYED")
                .pricingCurrency("USD").pricingBaseFare(new BigDecimal("680.00"))
                .availableSeatsEconomy(200).availableSeatsBusiness(30).availableSeatsFirst(12)
                .tags(new ArrayList<>(List.of("direct", "overnight", "long-haul")))
                .build();
        Segment f5s1 = Segment.builder()
                .segmentNumber(1)
                .departureAirportCode("JFK").departureAirportName("John F. Kennedy International Airport").departureTerminal("1").departureDateTime("2026-10-20T22:00:00")
                .arrivalAirportCode("CDG").arrivalAirportName("Charles de Gaulle Airport").arrivalTerminal("2E").arrivalDateTime("2026-10-21T11:30:00")
                .aircraftModel("Airbus A350-900").aircraftRegistration("N-SKBF")
                .seatConfigEconomy(200).seatConfigBusiness(30).seatConfigFirst(12)
                .durationMinutes(450).flight(f5).build();
        f5.setSegments(new ArrayList<>(List.of(f5s1)));
        Tax f5t1 = Tax.builder().code("US").name("US Transportation Tax").amount(new BigDecimal("56.00")).flight(f5).build();
        Tax f5t2 = Tax.builder().code("XF").name("Passenger Facility Charge").amount(new BigDecimal("4.50")).flight(f5).build();
        f5.setTaxes(new ArrayList<>(List.of(f5t1, f5t2)));
        Fee f5fee1 = Fee.builder().feeCode("MEAL").description("Premium meal service").amount(new BigDecimal("35.00")).optional(true).flight(f5).build();
        f5.setFees(new ArrayList<>(List.of(f5fee1)));
        // BUG-08: should be 680+60.5+35 = 775.50, bug: 680+60.5 = 740.50
        f5.setPricingTotalAmount(new BigDecimal("740.50"));

        flightRepository.saveAll(List.of(f1, f2, f3, f4, f5));
    }

    private void seedBookings() {
        // Booking 1: CONFIRMED on Flight 1 (WAW→FRA)
        Booking b1 = Booking.builder()
                .bookingReference("SB-ABC123").flightId(1L).status("CONFIRMED")
                .paymentMethod("CREDIT_CARD").paymentStatus("COMPLETED")
                .paymentAmount(new BigDecimal("193.95")).paymentCurrency("EUR")
                .cardLastFour("4242").cardBrand("VISA").cardHolderName("Jan Kowalski")
                .pricingSummaryCurrency("EUR")
                .pricingSummaryBaseFare(new BigDecimal("120.00"))
                .pricingSummaryTotalTaxes(new BigDecimal("65.50"))
                .pricingSummaryTotalFees(new BigDecimal("30.00"))
                .pricingSummaryDiscountAmount(new BigDecimal("21.55"))
                .pricingSummaryGrandTotal(new BigDecimal("193.95"))
                .build();
        Passenger b1p1 = Passenger.builder()
                .sequenceNumber(1)
                .firstName("Jan").lastName("Kowalski")
                .dateOfBirth("1990-05-15").nationality("PL")
                .email("jan@example.com").phone("+48123456789")
                .seatNumber("12A").seatClass("ECONOMY").seatType("WINDOW")
                .specialRequests(new ArrayList<>(List.of("VEGETARIAN_MEAL", "WHEELCHAIR_ASSISTANCE")))
                .booking(b1).build();
        BaggageItem b1p1bag1 = BaggageItem.builder().type("CHECKED").weightKg(23).count(1).passenger(b1p1).build();
        BaggageItem b1p1bag2 = BaggageItem.builder().type("CABIN").weightKg(8).count(1).passenger(b1p1).build();
        b1p1.setBaggageItems(new ArrayList<>(List.of(b1p1bag1, b1p1bag2)));
        b1.setPassengers(new ArrayList<>(List.of(b1p1)));

        // Booking 2: PENDING on Flight 2 (LHR→JFK)
        Booking b2 = Booking.builder()
                .bookingReference("SB-DEF456").flightId(2L).status("PENDING")
                .paymentMethod("DEBIT_CARD").paymentStatus("PENDING")
                .paymentAmount(new BigDecimal("1048.00")).paymentCurrency("GBP")
                .cardLastFour("8888").cardBrand("MASTERCARD").cardHolderName("Emily Smith")
                .pricingSummaryCurrency("GBP")
                .pricingSummaryBaseFare(new BigDecimal("850.00"))
                .pricingSummaryTotalTaxes(new BigDecimal("198.00"))
                .pricingSummaryTotalFees(new BigDecimal("15.00"))
                .pricingSummaryDiscountAmount(new BigDecimal("0.00"))
                .pricingSummaryGrandTotal(new BigDecimal("1048.00"))
                .build();
        Passenger b2p1 = Passenger.builder()
                .sequenceNumber(1)
                .firstName("Emily").lastName("Smith")
                .dateOfBirth("1985-11-20").nationality("GB")
                .email("emily.smith@example.com").phone("+447700900123")
                .seatNumber("3A").seatClass("BUSINESS").seatType("WINDOW")
                .specialRequests(new ArrayList<>(List.of("KOSHER_MEAL")))
                .booking(b2).build();
        BaggageItem b2p1bag1 = BaggageItem.builder().type("CHECKED").weightKg(32).count(2).passenger(b2p1).build();
        BaggageItem b2p1bag2 = BaggageItem.builder().type("CABIN").weightKg(8).count(1).passenger(b2p1).build();
        b2p1.setBaggageItems(new ArrayList<>(List.of(b2p1bag1, b2p1bag2)));
        b2.setPassengers(new ArrayList<>(List.of(b2p1)));

        // Booking 3: CANCELLED on Flight 3 (CDG→BCN)
        Booking b3 = Booking.builder()
                .bookingReference("SB-GHI789").flightId(3L).status("CANCELLED")
                .paymentMethod("BLIK").paymentStatus("REFUNDED")
                .paymentAmount(new BigDecimal("44.63")).paymentCurrency("EUR")
                .pricingSummaryCurrency("EUR")
                .pricingSummaryBaseFare(new BigDecimal("45.00"))
                .pricingSummaryTotalTaxes(new BigDecimal("7.50"))
                .pricingSummaryTotalFees(new BigDecimal("0.00"))
                .pricingSummaryDiscountAmount(new BigDecimal("7.88"))
                .pricingSummaryGrandTotal(new BigDecimal("44.63"))
                .build();
        Passenger b3p1 = Passenger.builder()
                .sequenceNumber(1)
                .firstName("Marie").lastName("Dupont")
                .dateOfBirth("1992-03-08").nationality("FR")
                .email("marie.dupont@example.com").phone("+33612345678")
                .seatNumber("22C").seatClass("ECONOMY").seatType("AISLE")
                .specialRequests(new ArrayList<>())
                .booking(b3).build();
        BaggageItem b3p1bag1 = BaggageItem.builder().type("CABIN").weightKg(8).count(1).passenger(b3p1).build();
        b3p1.setBaggageItems(new ArrayList<>(List.of(b3p1bag1)));
        b3.setPassengers(new ArrayList<>(List.of(b3p1)));

        bookingRepository.saveAll(List.of(b1, b2, b3));
    }
}
