package com.example.statistics_service.controller;

import com.example.statistics_service.model.StatisticsSummary;
import com.example.statistics_service.model.TelemetryData;
import com.example.statistics_service.service.StatisticsService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
public class StatisticsController {

    private final StatisticsService service;

    public StatisticsController(StatisticsService service) {
        this.service = service;
    }

    @GetMapping("/houses/{houseId}/telemetry")
    public ResponseEntity<Page<TelemetryData>> getHouseTelemetry(
            @PathVariable Long houseId,
            @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startTime,
            @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime endTime,
            @RequestParam(defaultValue = "100") @Min(1) @Max(1000) int limit,
            @RequestParam(defaultValue = "0") @Min(0) int offset) {
        return ResponseEntity.ok(service.getHouseTelemetry(houseId, startTime, endTime, limit, offset / limit));
    }

    @GetMapping("/houses/{houseId}/devices/{deviceId}/telemetry")
    public ResponseEntity<Page<TelemetryData>> getDeviceTelemetry(
            @PathVariable Long houseId,
            @PathVariable Long deviceId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime endTime,
            @RequestParam(defaultValue = "100") @Min(1) @Max(1000) int limit,
            @RequestParam(defaultValue = "0") @Min(0) int offset)
    {
        return ResponseEntity.ok(service.getDeviceTelemetry(deviceId, startTime, endTime, 100, 0 ));
    }

    @GetMapping("/houses/{houseId}/statistics/summary")
    public ResponseEntity<List<StatisticsSummary>> getHouseStatisticsSummary(
            @PathVariable Long houseId,
            @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startTime,
            @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime endTime) {
        return ResponseEntity.ok(service.getHouseStatisticsSummary(houseId, startTime, endTime));
    }
}