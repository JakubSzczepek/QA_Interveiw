package com.recruitment.skybook.controller;

import com.recruitment.skybook.config.ApiLogStore;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST endpoint to retrieve and manage API logs.
 * Hidden from Swagger (internal/recruiter tool).
 */
@RestController
@RequestMapping("/api/v1/logs")
@RequiredArgsConstructor
@Hidden
public class LogController {

    private final ApiLogStore logStore;

    @GetMapping
    public ResponseEntity<List<ApiLogStore.LogEntry>> getLogs() {
        return ResponseEntity.ok(logStore.getAll());
    }

    @DeleteMapping
    public ResponseEntity<Map<String, String>> clearLogs() {
        int count = logStore.size();
        logStore.clear();
        return ResponseEntity.ok(Map.of("message", "Cleared " + count + " log entries"));
    }
}
