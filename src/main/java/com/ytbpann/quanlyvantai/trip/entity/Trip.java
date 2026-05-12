package com.ytbpann.quanlyvantai.trip.entity;

import com.ytbpann.quanlyvantai.location.entity.LocationPoint;
import com.ytbpann.quanlyvantai.user.entity.UserAccount;
import com.ytbpann.quanlyvantai.vehicle.entity.Vehicle;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "trips",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_trips_trip_code", columnNames = "trip_code")
        }
)
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "trip_code", nullable = false, length = 50)
    private String tripCode;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "driver_id", nullable = false)
    private UserAccount driver;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    /*
     * Giữ lại 2 field text cũ để không mất dữ liệu Trip Phase 1.
     * Sang Phase 2, khi tạo/sửa Trip bằng Location, service sẽ tự điền text
     * từ pickupLocation và deliveryLocation để tránh lỗi NOT NULL trong database.
     */
    @Column(name = "departure_point", nullable = false, length = 255)
    private String departurePoint;

    @Column(name = "destination_point", nullable = false, length = 255)
    private String destinationPoint;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pickup_location_id")
    private LocationPoint pickupLocation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_location_id")
    private LocationPoint deliveryLocation;

    @Column(name = "planned_start_time", nullable = false)
    private LocalDateTime plannedStartTime;

    @Column(name = "planned_end_time", nullable = false)
    private LocalDateTime plannedEndTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private TripStatus status = TripStatus.PLANNED;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    public Trip() {
    }

    public Long getId() {
        return id;
    }

    public String getTripCode() {
        return tripCode;
    }

    public void setTripCode(String tripCode) {
        this.tripCode = tripCode;
    }

    public UserAccount getDriver() {
        return driver;
    }

    public void setDriver(UserAccount driver) {
        this.driver = driver;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public String getDeparturePoint() {
        return departurePoint;
    }

    public void setDeparturePoint(String departurePoint) {
        this.departurePoint = departurePoint;
    }

    public String getDestinationPoint() {
        return destinationPoint;
    }

    public void setDestinationPoint(String destinationPoint) {
        this.destinationPoint = destinationPoint;
    }

    public LocationPoint getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(LocationPoint pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public LocationPoint getDeliveryLocation() {
        return deliveryLocation;
    }

    public void setDeliveryLocation(LocationPoint deliveryLocation) {
        this.deliveryLocation = deliveryLocation;
    }

    public LocalDateTime getPlannedStartTime() {
        return plannedStartTime;
    }

    public void setPlannedStartTime(LocalDateTime plannedStartTime) {
        this.plannedStartTime = plannedStartTime;
    }

    public LocalDateTime getPlannedEndTime() {
        return plannedEndTime;
    }

    public void setPlannedEndTime(LocalDateTime plannedEndTime) {
        this.plannedEndTime = plannedEndTime;
    }

    public TripStatus getStatus() {
        return status;
    }

    public void setStatus(TripStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}