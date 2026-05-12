package com.ytbpann.quanlyvantai.trip.dto;

import com.ytbpann.quanlyvantai.trip.entity.TripStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class TripForm {

    /*
     * Phase 2:
     * Mã chuyến sẽ được tự sinh theo dạng:
     * pickupCode-deliveryCode-yyyyMMdd-sequence
     *
     * Vì vậy không còn bắt buộc nhập mã chuyến từ form nữa.
     * Field này vẫn giữ lại để hiển thị khi sửa chuyến hoặc tương thích dữ liệu cũ.
     */
    @Size(max = 50, message = "Mã chuyến không được vượt quá 50 ký tự")
    private String tripCode;

    @NotNull(message = "Vui lòng chọn tài xế")
    private Long driverId;

    @NotNull(message = "Vui lòng chọn xe")
    private Long vehicleId;

    @NotNull(message = "Vui lòng chọn điểm lấy hàng")
    private Long pickupLocationId;

    @NotNull(message = "Vui lòng chọn điểm giao hàng")
    private Long deliveryLocationId;

    /*
     * Giữ lại 2 field cũ để không phá dữ liệu Trip Phase 1.
     * Sang Phase 2, service sẽ tự điền từ LocationPoint.
     */
    @Size(max = 255, message = "Điểm đi không được vượt quá 255 ký tự")
    private String departurePoint;

    @Size(max = 255, message = "Điểm đến không được vượt quá 255 ký tự")
    private String destinationPoint;

    @NotNull(message = "Vui lòng nhập thời gian dự kiến bắt đầu")
    @FutureOrPresent(message = "Thời gian bắt đầu không nên nằm trong quá khứ")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime plannedStartTime;

    @NotNull(message = "Vui lòng nhập thời gian dự kiến kết thúc")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime plannedEndTime;

    @NotNull(message = "Vui lòng chọn trạng thái")
    private TripStatus status = TripStatus.PLANNED;

    @Size(max = 2000, message = "Ghi chú không được vượt quá 2000 ký tự")
    private String notes;

    public String getTripCode() {
        return tripCode;
    }

    public void setTripCode(String tripCode) {
        this.tripCode = tripCode;
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

    public Long getPickupLocationId() {
        return pickupLocationId;
    }

    public void setPickupLocationId(Long pickupLocationId) {
        this.pickupLocationId = pickupLocationId;
    }

    public Long getDeliveryLocationId() {
        return deliveryLocationId;
    }

    public void setDeliveryLocationId(Long deliveryLocationId) {
        this.deliveryLocationId = deliveryLocationId;
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