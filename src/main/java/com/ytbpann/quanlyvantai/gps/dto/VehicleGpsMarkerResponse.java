package com.ytbpann.quanlyvantai.gps.dto;

import com.ytbpann.quanlyvantai.driver.entity.DriverProfile;
import com.ytbpann.quanlyvantai.gps.entity.GpsLocationLog;
import com.ytbpann.quanlyvantai.location.entity.LocationPoint;
import com.ytbpann.quanlyvantai.trip.entity.Trip;
import com.ytbpann.quanlyvantai.vehicle.entity.Vehicle;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class VehicleGpsMarkerResponse {

    private Long gpsLogId;

    private Long vehicleId;

    private String licensePlate;

    private String vehicleType;

    private String vehicleCapacity;

    private Long driverProfileId;

    private String driverCode;

    private String driverFullName;

    private Long tripId;

    private String tripCode;

    private String tripStatus;

    private String tripStatusDisplayName;

    private Long pickupLocationId;

    private String pickupCode;

    private String pickupName;

    private String pickupAddress;

    private Double pickupLatitude;

    private Double pickupLongitude;

    private Long deliveryLocationId;

    private String deliveryCode;

    private String deliveryName;

    private String deliveryAddress;

    private Double deliveryLatitude;

    private Double deliveryLongitude;

    private Double latitude;

    private Double longitude;

    private Double accuracyMeter;

    private Double speedMeterPerSecond;

    private Double headingDegree;

    private String source;

    private LocalDateTime recordedAt;

    private LocalDateTime createdAt;

    public static VehicleGpsMarkerResponse from(GpsLocationLog log) {
        VehicleGpsMarkerResponse response = new VehicleGpsMarkerResponse();

        response.setGpsLogId(log.getId());
        response.setLatitude(log.getLatitude());
        response.setLongitude(log.getLongitude());
        response.setAccuracyMeter(log.getAccuracyMeter());
        response.setSpeedMeterPerSecond(log.getSpeedMeterPerSecond());
        response.setHeadingDegree(log.getHeadingDegree());
        response.setSource(log.getSource());
        response.setRecordedAt(log.getRecordedAt());
        response.setCreatedAt(log.getCreatedAt());

        Vehicle vehicle = log.getVehicle();
        if (vehicle != null) {
            response.setVehicleId(vehicle.getId());
            response.setLicensePlate(vehicle.getLicensePlate());
            response.setVehicleType(vehicle.getVehicleType());
            response.setVehicleCapacity(toPlainString(vehicle.getCapacity()));
        }

        DriverProfile driver = log.getDriver();
        if (driver != null) {
            response.setDriverProfileId(driver.getId());
            response.setDriverCode(driver.getDriverCode());
            response.setDriverFullName(driver.getFullName());
        }

        Trip trip = log.getTrip();
        if (trip != null) {
            response.setTripId(trip.getId());
            response.setTripCode(trip.getTripCode());

            if (trip.getStatus() != null) {
                response.setTripStatus(trip.getStatus().name());
                response.setTripStatusDisplayName(trip.getStatus().getDisplayName());
            }

            fillPickupLocation(response, trip.getPickupLocation());
            fillDeliveryLocation(response, trip.getDeliveryLocation());
        }

        return response;
    }

    private static void fillPickupLocation(VehicleGpsMarkerResponse response, LocationPoint pickupLocation) {
        if (pickupLocation == null) {
            return;
        }

        response.setPickupLocationId(pickupLocation.getId());
        response.setPickupCode(pickupLocation.getCode());
        response.setPickupName(pickupLocation.getName());
        response.setPickupAddress(pickupLocation.getAddress());
        response.setPickupLatitude(toDouble(pickupLocation.getLatitude()));
        response.setPickupLongitude(toDouble(pickupLocation.getLongitude()));
    }

    private static void fillDeliveryLocation(VehicleGpsMarkerResponse response, LocationPoint deliveryLocation) {
        if (deliveryLocation == null) {
            return;
        }

        response.setDeliveryLocationId(deliveryLocation.getId());
        response.setDeliveryCode(deliveryLocation.getCode());
        response.setDeliveryName(deliveryLocation.getName());
        response.setDeliveryAddress(deliveryLocation.getAddress());
        response.setDeliveryLatitude(toDouble(deliveryLocation.getLatitude()));
        response.setDeliveryLongitude(toDouble(deliveryLocation.getLongitude()));
    }

    private static Double toDouble(BigDecimal value) {
        return value == null ? null : value.doubleValue();
    }

    private static String toPlainString(BigDecimal value) {
        return value == null ? null : value.stripTrailingZeros().toPlainString();
    }

    public Long getGpsLogId() {
        return gpsLogId;
    }

    public void setGpsLogId(Long gpsLogId) {
        this.gpsLogId = gpsLogId;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleCapacity() {
        return vehicleCapacity;
    }

    public void setVehicleCapacity(String vehicleCapacity) {
        this.vehicleCapacity = vehicleCapacity;
    }

    public Long getDriverProfileId() {
        return driverProfileId;
    }

    public void setDriverProfileId(Long driverProfileId) {
        this.driverProfileId = driverProfileId;
    }

    public String getDriverCode() {
        return driverCode;
    }

    public void setDriverCode(String driverCode) {
        this.driverCode = driverCode;
    }

    public String getDriverFullName() {
        return driverFullName;
    }

    public void setDriverFullName(String driverFullName) {
        this.driverFullName = driverFullName;
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

    public String getTripStatus() {
        return tripStatus;
    }

    public void setTripStatus(String tripStatus) {
        this.tripStatus = tripStatus;
    }

    public String getTripStatusDisplayName() {
        return tripStatusDisplayName;
    }

    public void setTripStatusDisplayName(String tripStatusDisplayName) {
        this.tripStatusDisplayName = tripStatusDisplayName;
    }

    public Long getPickupLocationId() {
        return pickupLocationId;
    }

    public void setPickupLocationId(Long pickupLocationId) {
        this.pickupLocationId = pickupLocationId;
    }

    public String getPickupCode() {
        return pickupCode;
    }

    public void setPickupCode(String pickupCode) {
        this.pickupCode = pickupCode;
    }

    public String getPickupName() {
        return pickupName;
    }

    public void setPickupName(String pickupName) {
        this.pickupName = pickupName;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public Double getPickupLatitude() {
        return pickupLatitude;
    }

    public void setPickupLatitude(Double pickupLatitude) {
        this.pickupLatitude = pickupLatitude;
    }

    public Double getPickupLongitude() {
        return pickupLongitude;
    }

    public void setPickupLongitude(Double pickupLongitude) {
        this.pickupLongitude = pickupLongitude;
    }

    public Long getDeliveryLocationId() {
        return deliveryLocationId;
    }

    public void setDeliveryLocationId(Long deliveryLocationId) {
        this.deliveryLocationId = deliveryLocationId;
    }

    public String getDeliveryCode() {
        return deliveryCode;
    }

    public void setDeliveryCode(String deliveryCode) {
        this.deliveryCode = deliveryCode;
    }

    public String getDeliveryName() {
        return deliveryName;
    }

    public void setDeliveryName(String deliveryName) {
        this.deliveryName = deliveryName;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public Double getDeliveryLatitude() {
        return deliveryLatitude;
    }

    public void setDeliveryLatitude(Double deliveryLatitude) {
        this.deliveryLatitude = deliveryLatitude;
    }

    public Double getDeliveryLongitude() {
        return deliveryLongitude;
    }

    public void setDeliveryLongitude(Double deliveryLongitude) {
        this.deliveryLongitude = deliveryLongitude;
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