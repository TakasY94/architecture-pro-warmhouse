package com.example.statistics_service.repository;

import com.example.statistics_service.model.StatisticsSummary;
import com.example.statistics_service.model.TelemetryData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface TelemetryRepository extends JpaRepository<TelemetryData, Long> {

    @Query("SELECT t FROM TelemetryData t JOIN t.measurementUnit mu JOIN Device d ON t.deviceId = d.deviceId " +
            "WHERE d.houseId = :houseId AND t.timestamp BETWEEN :startTime AND :endTime")
    Page<TelemetryData> findByHouseIdAndTimestampBetween(Long houseId, OffsetDateTime startTime, OffsetDateTime endTime, Pageable pageable);

    Page<TelemetryData> findByDeviceIdAndTimestampBetween(Long deviceId, OffsetDateTime startTime, OffsetDateTime endTime, Pageable pageable);

    @Query("SELECT new com.example.statistics_service.model.StatisticsSummary(t.deviceId, t.measurementUnit.unitId, t.measurementUnit.name, " +
            "AVG(t.value), MIN(t.value), MAX(t.value), COUNT(t)) " +
            "FROM TelemetryData t JOIN Device d ON t.deviceId = d.deviceId " +
            "WHERE d.houseId = :houseId AND t.timestamp BETWEEN :startTime AND :endTime " +
            "GROUP BY t.deviceId, t.measurementUnit.unitId, t.measurementUnit.name")
    List<StatisticsSummary> findSummaryByHouseIdAndTimestampBetween(Long houseId, OffsetDateTime startTime, OffsetDateTime endTime);
}
