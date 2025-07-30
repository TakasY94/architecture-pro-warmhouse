package com.example.statistics_service.model;


import lombok.Data;

@Data
public class StatisticsSummary {
    private Long deviceId;
    private Long unitId;
    private String unitName;
    private Double averageValue;
    private Double minValue;
    private Double maxValue;
    private Long count;

    public StatisticsSummary(Long deviceId, Long unitId, String unitName, Double averageValue, Double minValue, Double maxValue, Long count) {
        this.deviceId = deviceId;
        this.unitId = unitId;
        this.unitName = unitName;
        this.averageValue = averageValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.count = count;
    }
}