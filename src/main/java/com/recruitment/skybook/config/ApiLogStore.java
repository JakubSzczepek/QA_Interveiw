package com.recruitment.skybook.config;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicLong;

/**
 * In-memory circular buffer for API request/response logs.
 * Stores the last MAX_ENTRIES entries. Thread-safe.
 */
@Component
public class ApiLogStore {

    private static final int MAX_ENTRIES = 500;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    private final Deque<LogEntry> entries = new ConcurrentLinkedDeque<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    public void add(String method, String uri, String queryString,
                    int status, String requestBody, String responseBody,
                    long durationMs) {
        String fullUrl = queryString != null && !queryString.isEmpty()
                ? uri + "?" + queryString : uri;

        LogEntry entry = new LogEntry(
                idCounter.getAndIncrement(),
                LocalDateTime.now().format(FMT),
                method,
                fullUrl,
                status,
                truncate(requestBody, 10_000),
                truncate(responseBody, 10_000),
                durationMs
        );

        entries.addFirst(entry);

        // Trim to max size
        while (entries.size() > MAX_ENTRIES) {
            entries.removeLast();
        }
    }

    public List<LogEntry> getAll() {
        return new ArrayList<>(entries);
    }

    public void clear() {
        entries.clear();
    }

    public int size() {
        return entries.size();
    }

    private String truncate(String s, int maxLen) {
        if (s == null) return null;
        if (s.length() <= maxLen) return s;
        return s.substring(0, maxLen) + "... [truncated]";
    }

    public record LogEntry(
            long id,
            String timestamp,
            String method,
            String url,
            int status,
            String requestBody,
            String responseBody,
            long durationMs
    ) {}
}
