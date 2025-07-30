package com.example.statistics_service.service;

import com.example.statistics_service.errors.ResourceNotFoundException;
import com.example.statistics_service.model.StatisticsSummary;
import com.example.statistics_service.model.TelemetryData;
import com.example.statistics_service.repository.DeviceRepository;
import com.example.statistics_service.repository.TelemetryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class StatisticsService {

    private static final Logger logger = LoggerFactory.getLogger(StatisticsService.class);

    private final TelemetryRepository telemetryRepository;
    private final DeviceRepository deviceRepository;

    public StatisticsService(TelemetryRepository telemetryRepository, DeviceRepository deviceRepository) {
        this.telemetryRepository = telemetryRepository;
        this.deviceRepository = deviceRepository;
    }

    public Page<TelemetryData> getHouseTelemetry(Long houseId, OffsetDateTime startTime, OffsetDateTime endTime, int pageSize, int pageNumber) {
        logger.info("Fetching telemetry for houseId: {}, startTime: {}, endTime: {}, pageSize: {}, pageNumber: {}", houseId, startTime, endTime, pageSize, pageNumber);
        if (endTime.isBefore(startTime)) {
            logger.error("Invalid time range: endTime {} is before startTime {}", endTime, startTime);
            throw new IllegalArgumentException("End time must be after start time");
        }
        if (!deviceRepository.existsByHouseId(houseId)) {
            logger.error("House not found: {}", houseId);
            throw new ResourceNotFoundException("House not found");
        }
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return telemetryRepository.findByHouseIdAndTimestampBetween(houseId, startTime, endTime, pageable);
    }

    public Page<TelemetryData> getDeviceTelemetry(Long deviceId, OffsetDateTime startTime, OffsetDateTime endTime, int pageSize, int pageNumber) {
        logger.info("Fetching telemetry for deviceId: {}, startTime: {}, endTime: {}, pageSize: {}, pageNumber: {}", deviceId, startTime, endTime, pageSize, pageNumber);
        if (endTime.isBefore(startTime)) {
            logger.error("Invalid time range: endTime {} is before startTime {}", endTime, startTime);
            throw new IllegalArgumentException("End time must be after start time");
        }
        if (!deviceRepository.existsById(deviceId)) {
            logger.error("Device not found: {}", deviceId);
            throw new ResourceNotFoundException("Device not found");
        }
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return telemetryRepository.findByDeviceIdAndTimestampBetween(deviceId, startTime, endTime, pageable);
    }

    public List<StatisticsSummary> getHouseStatisticsSummary(Long houseId, OffsetDateTime startTime, OffsetDateTime endTime) {
        logger.info("Fetching statistics summary for houseId: {}, startTime: {}, endTime: {}", houseId, startTime, endTime);
        if (endTime.isBefore(startTime)) {
            logger.error("Invalid time range: endTime {} is before startTime {}", endTime, startTime);
            throw new IllegalArgumentException("End time must be after start time");
        }
        if (!deviceRepository.existsByHouseId(houseId)) {
            logger.error("House not found: {}", houseId);
            throw new ResourceNotFoundException("House not found");
        }
        return telemetryRepository.findSummaryByHouseIdAndTimestampBetween(houseId, startTime, endTime);
    }
}
