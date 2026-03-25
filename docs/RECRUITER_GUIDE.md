# 🔒 SkyBook API — Arkusz rekrutera (POUFNE)

> **⚠ Ten dokument NIE jest przeznaczony dla kandydata.**
> Zawiera odpowiedzi (listę bugów), kryteria oceny i wskazówki prowadzenia zadania.

---

## 1. Przygotowanie przed rozmową

| Krok | Opis |
|------|------|
| **5 min przed** | Uruchom aplikację / zrób "ping" na API (cold start na Render ~30s) |
| **Restart DB** | Zrestartuj usługę, aby zresetować dane do stanu początkowego |
| **Materiały** | Wydrukuj/wyślij kandydatowi: `CANDIDATE_TASK.md` (reguły + endpointy) |
| **Narzędzia** | Kandydat potrzebuje tylko przeglądarki — REST Client jest wbudowany na `/` |

---

## 2. Przebieg zadania (30 min)

| Czas | Faza | Notatki rekrutera |
|------|------|-------------------|
| 0–2 min | **Wprowadzenie** | „SkyBook to API systemu rezerwacji lotów. Na stronie głównej masz wbudowany klient REST z gotowymi requestami, które możesz modyfikować i wysyłać. Jest też Swagger UI. Otrzymujesz reguły biznesowe. Przetestuj API i zaraportuj znalezione problemy." |
| 2–25 min | **Testowanie** | Obserwuj podejście. Notuj: czy ma strategię, czy testuje random. Odpowiadaj na pytania o kontekst biznesowy, ale nie naprowadzaj na bugi. |
| 25–30 min | **Podsumowanie** | Kandydat prezentuje bugi. Notuj jakość raportów. |

### Pytania follow-up (opcjonalne, po zadaniu):
1. „Jakie testy zautomatyzowałbyś w pierwszej kolejności?"
2. „Jak zorganizowałbyś regression test suite dla tego API?"
3. „Jakie testy niefunkcjonalne byś dodał?" (performance, security)
4. „Jak podszedłbyś do testowania tego API w CI/CD pipeline?"
5. „Jak przetestowałbyś poprawność kalkulacji cenowych (taxes + fees + discount)?"

---

## 3. Lista osadzonych bugów (12 bugów)

### 🔴 Krytyczne (4)

| ID | Złamana reguła | Bug | Jak zreprodukować |
|----|---------------|-----|-------------------|
| **BUG-01** | BR-13 | **Duplikaty miejsc** — ten sam `seatNumber` może być przypisany wielu pasażerom na tym samym locie | `POST /api/v1/bookings` — dwa razy z tym samym `seatAssignment.seatNumber` ("12A") dla tego samego `flightId`. Oba przyjęte → podwójna sprzedaż miejsca. |
| **BUG-02** | BR-16 | **Usunięcie lotu z rezerwacjami** — lot z powiązanymi bookings może być usunięty | `DELETE /api/v1/flights/2` → 204 No Content, ale booking z `flightId: 2` nadal istnieje z martwym referencją. Powinno zwrócić 400/409. |
| **BUG-03** | BR-10 | **Cancel nie zwalnia miejsc** — anulowanie rezerwacji nie przywraca `availableSeats` | 1. `GET /api/v1/flights/2` → zapamiętaj `availableSeats` 2. `PATCH /api/v1/bookings/2/status` → `{"status": "CANCELLED"}` 3. `GET /api/v1/flights/2` → `availableSeats` bez zmian |
| **BUG-04** | BR-11 | **Pusta tablica pasażerów** — `"passengers": []` jest akceptowana | `POST /api/v1/bookings` z `"passengers": []` → 200 OK, booking bez pasażerów. Powinno: 400 "At least 1 passenger required". |

### 🟠 Średnie (5)

| ID | Złamana reguła | Bug | Jak zreprodukować |
|----|---------------|-----|-------------------|
| **BUG-05** | BR-06 | **Ujemna kwota podatku** — `taxes[].amount` akceptuje wartości < 0 | `POST /api/v1/flights` z `"taxes": [{"code": "TX", "name": "Tax", "amount": -50}]` → 200 OK. Powinno: 400. |
| **BUG-06** | BR-09 | **Discount > 100%** — `discount.percentage` akceptuje wartości poza 0–100 | `POST /api/v1/flights` z `"discount": {"code": "BIG", "percentage": 200, "validUntil": "2026-12-31"}` → 200 OK. Powinno: 400. |
| **BUG-07** | BR-17 | **EUR→GBP kurs = 1.0** — przelicznik EUR na GBP jest błędny | `GET /api/v1/pricing/convert?amount=100&from=EUR&to=GBP` → `convertedAmount: 100.00`, `exchangeRate: 1.0`. Powinno: ~86.14 (kurs 0.8614). |
| **BUG-08** | BR-08 | **totalAmount ignoruje fees** — w kalkulacji brakuje opłat | `GET /api/v1/flights/1` → `totalAmount = baseFare + Σtaxes - discount`. Porównaj z ręcznym obliczeniem: `baseFare + Σtaxes + Σfees - discount`. Różnica = suma `fees[]`. |
| **BUG-09** | BR-20 | **POST zwraca 200 zamiast 201** — oba endpointy POST | `POST /api/v1/flights` lub `POST /api/v1/bookings` → HTTP 200. Powinno: 201 Created. |

### 🟡 Niskie (3)

| ID | Złamana reguła | Bug | Jak zreprodukować |
|----|---------------|-----|-------------------|
| **BUG-10** | BR-19 | **Case-sensitive search** — wyszukiwanie nie ignoruje wielkości liter | `GET /api/v1/flights/search?destination=Frankfurt` → wyniki. `GET /api/v1/flights/search?destination=frankfurt` → 0 wyników. |
| **BUG-11** | BR-20 | **Puste body → 500** — `{}` na POST booking daje Internal Server Error | `POST /api/v1/bookings` z body `{}` → 500. Powinno: 400 z komunikatem walidacyjnym. |
| **BUG-12** | BR-14 | **null payment → 500** — brak obiektu payment daje crash | `POST /api/v1/bookings` z `"payment": null` → 500. Powinno: 400 "Payment is required". |

---

## 4. Mapa bugów → głębokość testowania

Tabela pomaga ocenić, jak głęboko kandydat sięga w zagnieżdżone struktury:

| Poziom | Opis | Bugi | Typowe znalezienie |
|--------|------|------|--------------------|
| **Root** | Status codes, edge cases | BUG-09, BUG-11 | Łatwe — widać od razu |
| **Level 1** | Zagnieżdżone obiekty (`pricing`, `payment`, `passengers[]`) | BUG-04, BUG-10, BUG-12 | Wymaga świadomego testowania | 
| **Level 2** | Obiekty w obiektach (`taxes[]`, `discount`, `seatAssignment`) | BUG-01, BUG-05, BUG-06, BUG-08 | Wymaga analizy payload structure |
| **Cross-entity** | Relacje między zasobami (lot ↔ booking) | BUG-02, BUG-03 | Wymaga testowania lifecycle |
| **External** | Osobne endpointy (przeliczanie walut) | BUG-07 | Wymaga weryfikacji matematycznej |

---

## 5. Kryteria oceny

### 5.1 Podejście do testowania (0–10 pkt)

| Punkty | Opis |
|--------|------|
| 0–3 | Losowe klikanie, brak struktury |
| 4–6 | Testuje happy path + podstawowe negatywne scenariusze na poziomie root |
| 7–8 | Systematyczne: boundary values, equivalence partitioning, testuje nested obiekty i tablice |
| 9–10 | Pełne pokrycie: CRUD lifecycle, relacje cross-entity, paginacja, status codes, deep nested + currency |

### 5.2 Znalezione bugi (0–10 pkt)

| Punkty | Znalezione | Uwagi |
|--------|------------|-------|
| 0–2 | 0–2 bugi | Głównie przypadkowe |
| 3–4 | 3–4 bugi | Root/Level 1, oczywiste |
| 5–6 | 5–7 bugów | Mix krytycznych + średnich, sięga Level 2 |
| 7–8 | 8–10 bugów | Cross-entity + kalkulacje cenowe |
| 9–10 | 11–12 bugów | Pełna kategoryzacja, null/empty na nested + currency |

### 5.3 Raportowanie bugów (0–10 pkt)

| Punkty | Opis |
|--------|------|
| 0–3 | „Coś nie działa" — brak szczegółów |
| 4–6 | Opisuje kroki reprodukcji, ale brak expected/actual |
| 7–8 | Pełny raport: steps, expected, actual, severity |
| 9–10 | + sugeruje root cause, edge cases, impact biznesowy |

### 5.4 Narzędzia i techniki (0–5 pkt)

| Punkty | Opis |
|--------|------|
| 0–1 | Tylko wbudowany REST Client, bez modyfikacji requestów |
| 2–3 | Modyfikuje pre-filled requesty, testuje warianty; lub Swagger "Try it out" |
| 4–5 | + własne narzędzia (Postman/curl), zmienne, scripting, walidacja kalkulacji |

### 5.5 Komunikacja (0–5 pkt)

| Punkty | Opis |
|--------|------|
| 0–2 | Pracuje w ciszy, nie tłumaczy podejścia |
| 3–4 | Opisuje co robi i dlaczego |
| 5 | Proaktywnie pyta o wymagania, priorytetyzuje, argumentuje severity |

---

## 6. Arkusz punktacji

**Kandydat:** ______________________________ **Data:** ______________

| Kategoria | Max | Punkty | Notatki |
|-----------|-----|--------|---------|
| 5.1 Podejście do testowania | 10 | ___ | |
| 5.2 Znalezione bugi | 10 | ___ | |
| 5.3 Raportowanie bugów | 10 | ___ | |
| 5.4 Narzędzia i techniki | 5 | ___ | |
| 5.5 Komunikacja | 5 | ___ | |
| **SUMA** | **40** | **___** | |

| Wynik | Ocena |
|-------|-------|
| 0–15 | ❌ Nie spełnia wymagań |
| 16–24 | ⚠ Poniżej oczekiwań |
| 25–32 | ✅ Spełnia oczekiwania (Senior QA) |
| 33–40 | 🌟 Powyżej oczekiwań |

### Checklist znalezionych bugów:

| ☐ | ID | Severity | Opis skrótowy |
|---|-----|----------|---------------|
| ☐ | BUG-01 | 🔴 Critical | Duplikaty miejsc (brak walidacji seatNumber) |
| ☐ | BUG-02 | 🔴 Critical | DELETE lotu z rezerwacjami się udaje |
| ☐ | BUG-03 | 🔴 Critical | Cancel booking nie zwalnia miejsc |
| ☐ | BUG-04 | 🔴 Critical | Pusta tablica passengers[] akceptowana |
| ☐ | BUG-05 | 🟠 Major | Ujemna kwota podatku akceptowana |
| ☐ | BUG-06 | 🟠 Major | Discount > 100% akceptowany |
| ☐ | BUG-07 | 🟠 Major | EUR→GBP kurs = 1.0 (powinien ~0.86) |
| ☐ | BUG-08 | 🟠 Major | totalAmount nie uwzględnia fees[] |
| ☐ | BUG-09 | 🟠 Major | POST zwraca 200 zamiast 201 |
| ☐ | BUG-10 | 🟡 Minor | Search case-sensitive |
| ☐ | BUG-11 | 🟡 Minor | Puste body {} → 500 |
| ☐ | BUG-12 | 🟡 Minor | null payment → 500 |

**Znalezione: ___ / 12** (Critical: ___/4, Major: ___/5, Minor: ___/3)

---

### Dodatkowe obserwacje rekrutera:

_Strategia testowania kandydata:_

____________________________________________________________

_Najciekawsze pytania/uwagi kandydata:_

____________________________________________________________

_Rekomendacja (HIRE / NO HIRE / UNDECIDED):_

____________________________________________________________

---

## 7. Lokalizacja bugów w kodzie

Dla kontekstu technicznego — gdzie dokładnie w kodzie są osadzone bugi:

| Bug | Plik | Szczegóły |
|-----|------|-----------|
| BUG-01 | `BookingService.java` | Brak walidacji unikalności `seatNumber` w `createBooking()` |
| BUG-02 | `FlightService.java` | `deleteFlight()` nie wywołuje `bookingRepository.existsByFlightId()` |
| BUG-03 | `BookingService.java` | `updateBookingStatus()` przy CANCELLED nie modyfikuje `availableSeats` |
| BUG-04 | `BookingService.java` | Brak `if (passengers.isEmpty())` w `createBooking()` |
| BUG-05 | `FlightService.java` | Brak walidacji `tax.amount >= 0` w `validateFlightRequest()` |
| BUG-06 | `FlightService.java` | Brak walidacji `discount.percentage` 0–100 w `validateFlightRequest()` |
| BUG-07 | `PricingService.java` | `EUR_RATES` map: `"GBP"` → `1.0` zamiast `0.8614` |
| BUG-08 | `FlightService.java` | `calculateTotalAmount()` pomija `fees[]` w sumowaniu |
| BUG-09 | `FlightController.java`, `BookingController.java` | `ResponseEntity.ok()` zamiast `.status(CREATED)` |
| BUG-10 | `FlightRepository.java` | JPQL `LIKE` bez `LOWER()` |
| BUG-11 | `BookingService.java` + `GlobalExceptionHandler.java` | NPE na `request.getPayment().getMethod()` gdy body={}, handler nie łapie NPE |
| BUG-12 | `BookingService.java` + `GlobalExceptionHandler.java` | NPE na `request.getPayment().getMethod()` gdy payment=null |

---

*Dokument poufny — tylko dla zespołu rekrutacyjnego*
*Wersja: 1.0 | Data: 2026-03-25*
