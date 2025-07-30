package com.example.statistics_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.OffsetDateTime;

@Entity
@Table(name = "telemetry_data")
@Data
public class TelemetryData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "telemetry_id")
    private Long telemetryId;

    @NotNull
    @Column(name = "device_id")
    private Long deviceId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "unit_id")
    private MeasurementUnit measurementUnit;

    @NotNull
    @Column(name = "value")
    private Double value;

    @NotNull
    @Column(name = "timestamp")
    private OffsetDateTime timestamp;

    public String getUnitName() {
        return measurementUnit != null ? measurementUnit.getName() : null;
    }
}