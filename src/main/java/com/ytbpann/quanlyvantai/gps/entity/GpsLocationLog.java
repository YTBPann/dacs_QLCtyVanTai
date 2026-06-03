package com.ytbpann.quanlyvantai.gps.entity;

import com.ytbpann.quanlyvantai.driver.entity.DriverProfile;
import com.ytbpann.quanlyvantai.trip.entity.Trip;
import com.ytbpann.quanlyvantai.vehicle.entity.Vehicle;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "gps_location_logs")
public class GpsLocationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Chuyến đang được theo dõi.
     * Có thể null để sau này vẫn lưu được vị trí tài xế/xe ngoài chuyến nếu cần.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    private Trip trip;

    /**
     * Hồ sơ tài xế gửi vị trí.
     * Trong project hiện tại tài xế được lưu bằng DriverProfile.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_profile_id")
    private DriverProfile driver;

    /**
     * Xe đang được theo dõi.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    /**
     * Độ chính xác GPS, đơn vị mét.
     */
    @Column(name = "accuracy_meter")
    private Double accuracyMeter;

    /**
     * Tốc độ do trình duyệt/thiết bị gửi lên, đơn vị m/s nếu có.
     */
    @Column(name = "speed_meter_per_second")
    private Double speedMeterPerSecond;

    /**
     * Hướng di chuyển, đơn vị độ nếu có.
     */
    @Column(name = "heading_degree")
    private Double headingDegree;

    /**
     * Nguồn gửi vị trí.
     * Ví dụ: DRIVER_BROWSER, MANUAL_TEST, MOBILE_BROWSER.
     */
    @Column(length = 50)
    private String source;

    /**
     * Thời điểm thiết bị ghi nhận vị trí.
     */
    @Column(name = "recorded_at", nullable = false)
    private LocalDateTime recordedAt;

    /**
     * Thời điểm server lưu vào DB.
     */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (recordedAt == null) {
            recordedAt = LocalDateTime.now();
        }

        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }

        if (source == null || source.isBlank()) {
            source = "UNKNOWN";
        }
    }

    public Long getId() {
        return id;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public DriverProfile getDriver() {
        return driver;
    }

    public void setDriver(DriverProfile driver) {
        this.driver = driver;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
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
}