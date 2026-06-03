package com.ytbpann.quanlyvantai.gps.dto;

import com.ytbpann.quanlyvantai.gps.entity.GpsLocationLog;

import java.time.LocalDateTime;

public class DriverGpsLocationResponse {

    private boolean success;

    private String message;

    private Long gpsLogId;

    private Long tripId;

    private String tripCode;

    private Long driverProfileId;

    private Long vehicleId;

    private Double latitude;

    private Double longitude;

    private Double accuracyMeter;

    private LocalDateTime recordedAt;

    public static DriverGpsLocationResponse success(GpsLocationLog log, String message) {
        DriverGpsLocationResponse response = new DriverGpsLocationResponse();

        response.setSuccess(true);
        response.setMessage(message);
        response.setGpsLogId(log.getId());

        if (log.getTrip() != null) {
            response.setTripId(log.getTrip().getId());
            response.setTripCode(log.getTrip().getTripCode());
        }

        if (log.getDriver() != null) {
            response.setDriverProfileId(log.getDriver().getId());
        }

        if (log.getVehicle() != null) {
            response.setVehicleId(log.getVehicle().getId());
        }

        response.setLatitude(log.getLatitude());
        response.setLongitude(log.getLongitude());
        response.setAccuracyMeter(log.getAccuracyMeter());
        response.setRecordedAt(log.getRecordedAt());

        return response;
    }

    public static DriverGpsLocationResponse error(String message) {
        DriverGpsLocationResponse response = new DriverGpsLocationResponse();
        response.setSuccess(false);
        response.setMessage(message);
        return response;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getGpsLogId() {
        return gpsLogId;
    }

    public void setGpsLogId(Long gpsLogId) {
        this.gpsLogId = gpsLogId;
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public String getTripCode() {
        return tripCode;
    }

    public void setTripCode(String tripCode) {
        this.tripCode = tripCode;
    }

    public Long getDriverProfileId() {
        return driverProfileId;
    }

    public void setDriverProfileId(Long driverProfileId) {
        this.driverProfileId = driverProfileId;
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

    public LocalDateTime getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(LocalDateTime recordedAt) {
        this.recordedAt = recordedAt;
    }
}