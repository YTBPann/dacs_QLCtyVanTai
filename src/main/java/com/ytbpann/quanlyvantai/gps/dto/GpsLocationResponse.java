package com.ytbpann.quanlyvantai.gps.dto;

import com.ytbpann.quanlyvantai.gps.entity.GpsLocationLog;

import java.time.LocalDateTime;

public class GpsLocationResponse {

    private Long id;

    private Long tripId;

    private Long driverId;

    private Long vehicleId;

    private Double latitude;

    private Double longitude;

    private Double accuracyMeter;

    private Double speedMeterPerSecond;

    private Double headingDegree;

    private String source;

    private LocalDateTime recordedAt;

    private LocalDateTime createdAt;

    public static GpsLocationResponse from(GpsLocationLog log) {
        GpsLocationResponse response = new GpsLocationResponse();

        response.setId(log.getId());

        if (log.getTrip() != null) {
            response.setTripId(log.getTrip().getId());
        }

        if (log.getDriver() != null) {
            response.setDriverId(log.getDriver().getId());
        }

        if (log.getVehicle() != null) {
            response.setVehicleId(log.getVehicle().getId());
        }

        response.setLatitude(log.getLatitude());
        response.setLongitude(log.getLongitude());
        response.setAccuracyMeter(log.getAccuracyMeter());
        response.setSpeedMeterPerSecond(log.getSpeedMeterPerSecond());
        response.setHeadingDegree(log.getHeadingDegree());
        response.setSource(log.getSource());
        response.setRecordedAt(log.getRecordedAt());
        response.setCreatedAt(log.getCreatedAt());

        return response;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getAccuracyMeter() {
        return accuracyMeter;
    }

    public void setAccuracyMeter(Double accuracyMeter) {
        this.accuracyMeter = accuracyMeter;
    }

    public Double getSpeedMeterPerSecond() {
        return speedMeterPerSecond;
    }

    public void setSpeedMeterPerSecond(Double speedMeterPerSecond) {
        this.speedMeterPerSecond = speedMeterPerSecond;
    }

    public Double getHeadingDegree() {
        return headingDegree;
    }

    public void setHeadingDegree(Double headingDegree) {
        this.headingDegree = headingDegree;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public LocalDateTime getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(LocalDateTime recordedAt) {
        this.recordedAt = recordedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
