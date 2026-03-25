# ✈ SkyBook API — Zadanie testowe QA

## Kontekst

**SkyBook** to REST API systemu rezerwacji lotów i zarządzania biletami lotniczymi. API obsługuje:

- Zarządzanie lotami (CRUD)
- Rezerwacje z pasażerami, miejscami i płatnościami
- Baza lotnisk
- Przeliczanie walut

## Twoje zadanie (30 min)

1. Zapoznaj się z API korzystając z dostępnych narzędzi
2. Przetestuj API pod kątem poprawności działania — szczególnie pod kątem poniższych reguł biznesowych
3. Zaraportuj znalezione błędy

## Dostępne narzędzia

| Narzędzie | URL | Opis |
|-----------|-----|------|
| **REST Client** | `/` (strona główna) | Wbudowany klient REST z gotowymi przykładowymi requestami — możesz je modyfikować i wysyłać |
| **Swagger UI** | `/swagger-ui.html` | Interaktywna dokumentacja API |
| **H2 Console** | `/h2-console` | Konsola bazy danych (JDBC URL: `jdbc:h2:mem:skybookdb`, user: `sa`, bez hasła) |

> 💡 **Wskazówka:** REST Client na stronie głównej ma 12 gotowych requestów — możesz je wysyłać od razu lub modyfikować. Nie musisz pisać JSON od zera.

---

## Endpointy API

### Flights (Loty)

| Method | Endpoint | Opis |
|--------|----------|------|
| GET | `/api/v1/flights?page=0&size=10` | Lista lotów (paginowana) |
| GET | `/api/v1/flights/{id}` | Szczegóły lotu |
| POST | `/api/v1/flights` | Utwórz lot |
| PUT | `/api/v1/flights/{id}` | Zaktualizuj lot |
| DELETE | `/api/v1/flights/{id}` | Usuń lot |
| GET | `/api/v1/flights/search?origin=...&destination=...&date=...` | Wyszukaj loty |

### Bookings (Rezerwacje)

| Method | Endpoint | Opis |
|--------|----------|------|
| GET | `/api/v1/bookings?page=0&size=10` | Lista rezerwacji |
| GET | `/api/v1/bookings/{id}` | Szczegóły rezerwacji |
| POST | `/api/v1/bookings` | Utwórz rezerwację |
| PATCH | `/api/v1/bookings/{id}/status` | Zmień status rezerwacji |

### Airports (Lotniska) — tylko odczyt

| Method | Endpoint | Opis |
|--------|----------|------|
| GET | `/api/v1/airports` | Lista lotnisk |
| GET | `/api/v1/airports/{code}` | Lotnisko po kodzie IATA |

### Pricing (Cennik)

| Method | Endpoint | Opis |
|--------|----------|------|
| GET | `/api/v1/pricing/convert?amount=100&from=EUR&to=USD` | Przelicz walutę |
| GET | `/api/v1/pricing/rates` | Aktualne kursy walut |

---

## Reguły biznesowe

Poniższe reguły opisują oczekiwane zachowanie API. Twoim zadaniem jest sprawdzić, czy API je spełnia.

| # | Reguła | Opis |
|---|--------|------|
| BR-01 | Numer lotu (`flightNumber`) | Wymagany, unikalny, format: 2–3 litery + `-` + 1–5 cyfr (np. `SB-1234`, `LO-345`) |
| BR-02 | Segmenty lotu (`segments[]`) | Min. 1 wymagany; lot bezpośredni = 1 segment, z przesiadką = 2+ |
| BR-03 | Segment: departure/arrival | Obiekty wymagane; `airportCode` – kod IATA 3 litery; `dateTime` – ISO 8601; `arrival.dateTime` musi być po `departure.dateTime`; `durationMinutes` > 0 |
| BR-04 | Segment: aircraft | Obiekt wymagany; `model` 1–100 znaków; `seatConfiguration` – wartości ≥ 0 dla każdej klasy |
| BR-05 | Cena bazowa i waluta | `pricing.baseFare` ≥ 0.00, max 2 miejsca po przecinku; `pricing.currency` ∈ {USD, EUR, GBP, PLN} |
| BR-06 | Podatki (`pricing.taxes[]`) | Opcjonalne; `code` 2 wielkie litery, unikalne w ramach lotu; `amount` ≥ 0.00 |
| BR-07 | Opłaty (`pricing.fees[]`) | Opcjonalne; `feeCode` unikalny; `amount` ≥ 0.00; max 10 opłat |
| BR-08 | Suma ceny (`pricing.totalAmount`) | Automatycznie kalkulowana: `baseFare + Σtaxes + Σfees − discount`; wartość w response musi być poprawna |
| BR-09 | Rabat (`pricing.discount`) | Opcjonalny; `percentage` 0–100; `validUntil` nie może być w przeszłości |
| BR-10 | Dostępne miejsca (`availableSeats`) | Automatycznie aktualizowane: zmniejszane przy rezerwacji, przywracane przy anulowaniu |
| BR-11 | Pasażerowie (`passengers[]`) | Min. 1, max 9 na rezerwację |
| BR-12 | Dane osobowe i kontakt pasażera | `personalInfo`: `firstName` i `lastName` 1–100 znaków, `dateOfBirth` ISO date, `nationality` alpha-2; `contact`: `email` poprawny format, `phone` format międzynarodowy |
| BR-13 | Przydział miejsca (`seatAssignment`) | Opcjonalny; `seatNumber` unikalny w ramach lotu; `class` ∈ {ECONOMY, BUSINESS, FIRST}; `type` ∈ {WINDOW, MIDDLE, AISLE} |
| BR-14 | Płatność (`payment`) | Obiekt wymagany; `method` ∈ {CREDIT_CARD, DEBIT_CARD, BANK_TRANSFER, BLIK}; `currency` ∈ {USD, EUR, GBP, PLN}; `cardDetails` wymagane gdy method = karta |
| BR-15 | Status rezerwacji | Dozwolone przejścia: PENDING → CONFIRMED, PENDING → CANCELLED, CONFIRMED → CANCELLED; nie można cofnąć CANCELLED |
| BR-16 | Usunięcie lotu | Nie można usunąć lotu, który ma powiązane rezerwacje |
| BR-17 | Przeliczanie walut | Endpoint `/api/v1/pricing/convert` musi poprawnie przeliczać między parami: USD, EUR, GBP, PLN |
| BR-18 | Paginacja | Domyślnie: page=0, size=10, max size=100 |
| BR-19 | Wyszukiwanie lotów | Case-insensitive, partial match po `origin`, `destination`, `date` |
| BR-20 | HTTP Status Codes | POST → 201, DELETE → 204, Not Found → 404, Validation → 400 |

---

## Oczekiwany format raportu błędu

Dla każdego znalezionego błędu podaj:

| Pole | Opis |
|------|------|
| **Tytuł** | Krótki, jednoznaczny opis problemu |
| **Kroki reprodukcji** | Numerowane kroki (endpoint, metoda, body) |
| **Oczekiwany rezultat** | Co powinno się stać wg reguł biznesowych |
| **Faktyczny rezultat** | Co faktycznie się dzieje |
| **Severity** | Critical / Major / Minor |

### Przykład raportu:

> **Tytuł:** GET /api/v1/flights zwraca 500 zamiast pustej listy gdy brak lotów
>
> **Kroki reprodukcji:**
> 1. Usuń wszystkie loty
> 2. Wyślij `GET /api/v1/flights`
>
> **Oczekiwany rezultat:** 200 OK z pustą listą `{ "content": [], "totalElements": 0 }`
>
> **Faktyczny rezultat:** 500 Internal Server Error
>
> **Severity:** Major

---

## Dane testowe

API posiada wstępnie załadowane dane:

- **6 lotnisk:** WAW, FRA, LHR, JFK, CDG, BCN
- **5 lotów:** SB-1234, SB-5678, SB-9012, SB-3456, SB-7890
- **3 rezerwacje** (statusy: CONFIRMED, PENDING, CANCELLED)
- **Waluty:** EUR (bazowa), USD, GBP, PLN

> ℹ **Uwaga:** Baza danych jest resetowana przy każdym restarcie API.

---

*Powodzenia! 🚀*
