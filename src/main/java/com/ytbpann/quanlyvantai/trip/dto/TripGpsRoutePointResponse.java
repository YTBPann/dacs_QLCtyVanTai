package com.ytbpann.quanlyvantai.trip.dto;

public class TripGpsRoutePointResponse {

    private Long id;
    private Long tripId;
    private Long driverProfileId;
    private Double latitude;
    private Double longitude;
    private Double accuracyMeter;
    private Double speedMeterPerSecond;
    private Double headingDegree;
    private String source;
    private String recordedAt;

    public TripGpsRoutePointResponse() {
    }

    public TripGpsRoutePointResponse(
            Long id,
            Long tripId,
            Long driverProfileId,
            Double latitude,
            Double longitude,
            Double accuracyMeter,
            Double speedMeterPerSecond,
            Double headingDegree,
            String source,
            String recordedAt
    ) {
        this.id = id;
        this.tripId = tripId;
        this.driverProfileId = driverProfileId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.accuracyMeter = accuracyMeter;
        this.speedMeterPerSecond = speedMeterPerSecond;
        this.headingDegree = headingDegree;
        this.source = source;
        this.recordedAt = recordedAt;
    }

    public Long getId() {
        return id;
    }

    public Long getTripId() {
        return tripId;
    }

    public Long getDriverProfileId() {
        return driverProfileId;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getAccuracyMeter() {
        return accuracyMeter;
    }

    public Double getSpeedMeterPerSecond() {
        return speedMeterPerSecond;
    }

    public Double getHeadingDegree() {
        return headingDegree;
    }

    public String getSource() {
        return source;
    }

    public String getRecordedAt() {
        return recordedAt;
    }
}