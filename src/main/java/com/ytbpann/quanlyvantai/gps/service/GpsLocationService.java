package com.ytbpann.quanlyvantai.gps.service;

import com.ytbpann.quanlyvantai.driver.entity.DriverProfile;
import com.ytbpann.quanlyvantai.driver.repository.DriverProfileRepository;
import com.ytbpann.quanlyvantai.gps.dto.DriverGpsLocationRequest;
import com.ytbpann.quanlyvantai.gps.dto.DriverGpsLocationResponse;
import com.ytbpann.quanlyvantai.gps.dto.GpsLocationResponse;
import com.ytbpann.quanlyvantai.gps.dto.GpsLocationUpdateRequest;
import com.ytbpann.quanlyvantai.gps.entity.GpsLocationLog;
import com.ytbpann.quanlyvantai.gps.repository.GpsLocationLogRepository;
import com.ytbpann.quanlyvantai.trip.entity.Trip;
import com.ytbpann.quanlyvantai.trip.entity.TripStatus;
import com.ytbpann.quanlyvantai.trip.repository.TripRepository;
import com.ytbpann.quanlyvantai.user.entity.UserAccount;
import com.ytbpann.quanlyvantai.vehicle.entity.Vehicle;
import com.ytbpann.quanlyvantai.vehicle.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ytbpann.quanlyvantai.gps.dto.VehicleGpsMarkerResponse;

import java.util.List;

@Service
public class GpsLocationService {

    private final GpsLocationLogRepository gpsLocationLogRepository;
    private final TripRepository tripRepository;
    private final DriverProfileRepository driverProfileRepository;
    private final VehicleRepository vehicleRepository;

    public GpsLocationService(
            GpsLocationLogRepository gpsLocationLogRepository,
            TripRepository tripRepository,
            DriverProfileRepository driverProfileRepository,
            VehicleRepository vehicleRepository
    ) {
        this.gpsLocationLogRepository = gpsLocationLogRepository;
        this.tripRepository = tripRepository;
        this.driverProfileRepository = driverProfileRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @Transactional
    public GpsLocationResponse saveLocation(GpsLocationUpdateRequest request) {
        validateAtLeastOneOwner(request);

        Trip trip = null;
        DriverProfile driver = null;
        Vehicle vehicle = null;

        if (request.getTripId() != null) {
            trip = tripRepository.findById(request.getTripId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy chuyến có ID: " + request.getTripId()));
        }

        if (request.getDriverId() != null) {
            driver = driverProfileRepository.findById(request.getDriverId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hồ sơ tài xế có ID: " + request.getDriverId()));
        }

        if (request.getVehicleId() != null) {
            vehicle = vehicleRepository.findById(request.getVehicleId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy xe có ID: " + request.getVehicleId()));
        }

        GpsLocationLog log = new GpsLocationLog();
        log.setTrip(trip);
        log.setDriver(driver);
        log.setVehicle(vehicle);
        log.setLatitude(request.getLatitude());
        log.setLongitude(request.getLongitude());
        log.setAccuracyMeter(request.getAccuracyMeter());
        log.setSpeedMeterPerSecond(request.getSpeedMeterPerSecond());
        log.setHeadingDegree(request.getHeadingDegree());
        log.setSource(normalizeSource(request.getSource()));
        log.setRecordedAt(request.getRecordedAt());

        GpsLocationLog savedLog = gpsLocationLogRepository.save(log);

        return GpsLocationResponse.from(savedLog);
    }

    @Transactional
    public DriverGpsLocationResponse saveDriverCurrentLocation(String username, DriverGpsLocationRequest request) {
        DriverProfile driverProfile = driverProfileRepository.findByLinkedUsernameWithLinkedUserAccount(username)
                .orElseThrow(() -> new IllegalArgumentException("Tài khoản hiện tại chưa được liên kết với hồ sơ tài xế"));

        if (!driverProfile.isActive()) {
            throw new IllegalArgumentException("Hồ sơ tài xế đang bị tắt, không thể cập nhật GPS");
        }

        UserAccount linkedUser = driverProfile.getLinkedUserAccount();

        if (linkedUser == null) {
            throw new IllegalArgumentException("Hồ sơ tài xế chưa liên kết tài khoản đăng nhập");
        }

        if (!linkedUser.isEnabled()) {
            throw new IllegalArgumentException("Tài khoản tài xế đang bị tắt, không thể cập nhật GPS");
        }

        Trip activeTrip = tripRepository.findTopByDriverAndStatusOrderByIdDesc(linkedUser, TripStatus.IN_PROGRESS)
                .orElse(null);

        GpsLocationLog log = new GpsLocationLog();
        log.setDriver(driverProfile);
        log.setLatitude(request.getLatitude());
        log.setLongitude(request.getLongitude());
        log.setAccuracyMeter(request.getAccuracyMeter());
        log.setSpeedMeterPerSecond(request.getSpeedMeterPerSecond());
        log.setHeadingDegree(request.getHeadingDegree());
        log.setSource(normalizeSource(request.getSource()));
        log.setRecordedAt(request.getRecordedAt());

        String message;

        if (activeTrip != null) {
            log.setTrip(activeTrip);
            log.setVehicle(activeTrip.getVehicle());
            message = "Đã cập nhật GPS cho chuyến đang vận chuyển";
        } else {
            message = "Đã cập nhật GPS cho tài xế, hiện chưa có chuyến đang vận chuyển";
        }

        GpsLocationLog savedLog = gpsLocationLogRepository.save(log);

        return DriverGpsLocationResponse.success(savedLog, message);
    }

    @Transactional(readOnly = true)
    public List<GpsLocationResponse> getLatestPerDriver() {
        return gpsLocationLogRepository.findLatestPerDriver()
                .stream()
                .map(GpsLocationResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<GpsLocationResponse> getLatestPerVehicle() {
        return gpsLocationLogRepository.findLatestPerVehicle()
                .stream()
                .map(GpsLocationResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<VehicleGpsMarkerResponse> getLatestVehicleMarkers() {
        return gpsLocationLogRepository.findLatestPerVehicle()
                .stream()
                .map(VehicleGpsMarkerResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<GpsLocationResponse> getTripHistory(Long tripId) {
        return gpsLocationLogRepository.findTop100ByTripIdOrderByRecordedAtDesc(tripId)
                .stream()
                .map(GpsLocationResponse::from)
                .toList();
    }

    private void validateAtLeastOneOwner(GpsLocationUpdateRequest request) {
        if (request.getTripId() == null && request.getDriverId() == null && request.getVehicleId() == null) {
            throw new IllegalArgumentException("Cần có ít nhất tripId, driverId hoặc vehicleId để lưu vị trí");
        }
    }

    private String normalizeSource(String source) {
        if (source == null || source.isBlank()) {
            return "DRIVER_BROWSER";
        }

        String normalized = source.trim();

        if (normalized.length() > 50) {
            return normalized.substring(0, 50);
        }

        return normalized;
    }
}