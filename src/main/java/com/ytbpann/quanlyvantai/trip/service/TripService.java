package com.ytbpann.quanlyvantai.trip.service;

import com.ytbpann.quanlyvantai.trip.dto.TripForm;
import com.ytbpann.quanlyvantai.trip.entity.Trip;
import com.ytbpann.quanlyvantai.trip.entity.TripStatus;
import com.ytbpann.quanlyvantai.trip.repository.TripRepository;
import com.ytbpann.quanlyvantai.user.entity.RoleName;
import com.ytbpann.quanlyvantai.user.entity.UserAccount;
import com.ytbpann.quanlyvantai.user.repository.UserAccountRepository;
import com.ytbpann.quanlyvantai.vehicle.entity.Vehicle;
import com.ytbpann.quanlyvantai.vehicle.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TripService {

    private static final List<TripStatus> ACTIVE_TRIP_STATUSES = List.of(
            TripStatus.PLANNED,
            TripStatus.IN_PROGRESS
    );

    private final TripRepository tripRepository;
    private final UserAccountRepository userAccountRepository;
    private final VehicleRepository vehicleRepository;

    public TripService(
            TripRepository tripRepository,
            UserAccountRepository userAccountRepository,
            VehicleRepository vehicleRepository
    ) {
        this.tripRepository = tripRepository;
        this.userAccountRepository = userAccountRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @Transactional(readOnly = true)
    public List<Trip> findAllTrips() {
        return tripRepository.findAllByOrderByPlannedStartTimeDesc();
    }

    @Transactional(readOnly = true)
    public Trip findById(Long id) {
        return tripRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy chuyến có ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<UserAccount> findAvailableDriversForCreate() {
        return userAccountRepository.findByRoleAndEnabledTrueOrderByFullNameAsc(RoleName.DRIVER)
                .stream()
                .filter(driver -> !tripRepository.existsByDriverAndStatusIn(driver, ACTIVE_TRIP_STATUSES))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Vehicle> findAvailableVehiclesForCreate() {
        return vehicleRepository.findByActiveTrueOrderByLicensePlateAsc()
                .stream()
                .filter(vehicle -> !tripRepository.existsByVehicleAndStatusIn(vehicle, ACTIVE_TRIP_STATUSES))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<UserAccount> findAvailableDriversForEdit(Long tripId) {
        return userAccountRepository.findByRoleAndEnabledTrueOrderByFullNameAsc(RoleName.DRIVER)
                .stream()
                .filter(driver -> !tripRepository.existsByDriverAndStatusInAndIdNot(driver, ACTIVE_TRIP_STATUSES, tripId))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Vehicle> findAvailableVehiclesForEdit(Long tripId) {
        return vehicleRepository.findByActiveTrueOrderByLicensePlateAsc()
                .stream()
                .filter(vehicle -> !tripRepository.existsByVehicleAndStatusInAndIdNot(vehicle, ACTIVE_TRIP_STATUSES, tripId))
                .toList();
    }

    public Trip createTrip(TripForm form) {
        validateTripCodeForCreate(form.getTripCode());
        validateTimeRange(form);

        UserAccount driver = findActiveDriver(form.getDriverId());
        Vehicle vehicle = findActiveVehicle(form.getVehicleId());

        TripStatus status = form.getStatus() == null ? TripStatus.PLANNED : form.getStatus();

        if (isActiveTripStatus(status)) {
            validateDriverNotInAnotherActiveTripForCreate(driver);
            validateVehicleNotInAnotherActiveTripForCreate(vehicle);
        }

        Trip trip = new Trip();
        applyFormToTrip(trip, form, driver, vehicle, status);

        return tripRepository.save(trip);
    }

    public Trip updateTrip(Long id, TripForm form) {
        Trip trip = findById(id);

        validateTripCodeForUpdate(form.getTripCode(), id);
        validateTimeRange(form);

        UserAccount driver = findActiveDriver(form.getDriverId());
        Vehicle vehicle = findActiveVehicle(form.getVehicleId());

        TripStatus status = form.getStatus() == null ? TripStatus.PLANNED : form.getStatus();

        if (isActiveTripStatus(status)) {
            validateDriverNotInAnotherActiveTripForUpdate(driver, id);
            validateVehicleNotInAnotherActiveTripForUpdate(vehicle, id);
        }

        applyFormToTrip(trip, form, driver, vehicle, status);

        return tripRepository.save(trip);
    }

    public Trip updateStatus(Long id, TripStatus status) {
        Trip trip = findById(id);

        if (status == null) {
            throw new IllegalArgumentException("Trạng thái chuyến không hợp lệ");
        }

        if (isActiveTripStatus(status)) {
            validateDriverNotInAnotherActiveTripForUpdate(trip.getDriver(), id);
            validateVehicleNotInAnotherActiveTripForUpdate(trip.getVehicle(), id);
        }

        trip.setStatus(status);
        return tripRepository.save(trip);
    }

    public TripForm toForm(Trip trip) {
        TripForm form = new TripForm();

        form.setTripCode(trip.getTripCode());
        form.setDriverId(trip.getDriver().getId());
        form.setVehicleId(trip.getVehicle().getId());
        form.setDeparturePoint(trip.getDeparturePoint());
        form.setDestinationPoint(trip.getDestinationPoint());
        form.setPlannedStartTime(trip.getPlannedStartTime());
        form.setPlannedEndTime(trip.getPlannedEndTime());
        form.setStatus(trip.getStatus());
        form.setNotes(trip.getNotes());

        return form;
    }

    private void applyFormToTrip(
            Trip trip,
            TripForm form,
            UserAccount driver,
            Vehicle vehicle,
            TripStatus status
    ) {
        trip.setTripCode(normalizeText(form.getTripCode()));
        trip.setDriver(driver);
        trip.setVehicle(vehicle);
        trip.setDeparturePoint(normalizeText(form.getDeparturePoint()));
        trip.setDestinationPoint(normalizeText(form.getDestinationPoint()));
        trip.setPlannedStartTime(form.getPlannedStartTime());
        trip.setPlannedEndTime(form.getPlannedEndTime());
        trip.setStatus(status);
        trip.setNotes(normalizeNullableText(form.getNotes()));
    }

    private UserAccount findActiveDriver(Long driverId) {
        UserAccount driver = userAccountRepository.findById(driverId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tài xế"));

        if (driver.getRole() != RoleName.DRIVER) {
            throw new IllegalArgumentException("Tài khoản được chọn không phải tài xế");
        }

        if (!driver.isEnabled()) {
            throw new IllegalArgumentException("Tài xế đang bị tắt, không thể gán chuyến");
        }

        return driver;
    }

    private Vehicle findActiveVehicle(Long vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy xe"));

        if (!vehicle.isActive()) {
            throw new IllegalArgumentException("Xe đang bị tắt, không thể gán chuyến");
        }

        return vehicle;
    }

    private void validateTripCodeForCreate(String tripCode) {
        String normalizedTripCode = normalizeText(tripCode);

        if (tripRepository.existsByTripCodeIgnoreCase(normalizedTripCode)) {
            throw new IllegalArgumentException("Mã chuyến đã tồn tại");
        }
    }

    private void validateTripCodeForUpdate(String tripCode, Long id) {
        String normalizedTripCode = normalizeText(tripCode);

        if (tripRepository.existsByTripCodeIgnoreCaseAndIdNot(normalizedTripCode, id)) {
            throw new IllegalArgumentException("Mã chuyến đã tồn tại");
        }
    }

    private void validateTimeRange(TripForm form) {
        if (form.getPlannedStartTime() == null || form.getPlannedEndTime() == null) {
            return;
        }

        if (!form.getPlannedEndTime().isAfter(form.getPlannedStartTime())) {
            throw new IllegalArgumentException("Thời gian kết thúc phải sau thời gian bắt đầu");
        }
    }

    private void validateDriverNotInAnotherActiveTripForCreate(UserAccount driver) {
        if (tripRepository.existsByDriverAndStatusIn(driver, ACTIVE_TRIP_STATUSES)) {
            throw new IllegalArgumentException("Tài xế này đang nằm trong chuyến chưa hoàn tất");
        }
    }

    private void validateVehicleNotInAnotherActiveTripForCreate(Vehicle vehicle) {
        if (tripRepository.existsByVehicleAndStatusIn(vehicle, ACTIVE_TRIP_STATUSES)) {
            throw new IllegalArgumentException("Xe này đang nằm trong chuyến chưa hoàn tất");
        }
    }

    private void validateDriverNotInAnotherActiveTripForUpdate(UserAccount driver, Long tripId) {
        if (tripRepository.existsByDriverAndStatusInAndIdNot(driver, ACTIVE_TRIP_STATUSES, tripId)) {
            throw new IllegalArgumentException("Tài xế này đang nằm trong chuyến chưa hoàn tất khác");
        }
    }

    private void validateVehicleNotInAnotherActiveTripForUpdate(Vehicle vehicle, Long tripId) {
        if (tripRepository.existsByVehicleAndStatusInAndIdNot(vehicle, ACTIVE_TRIP_STATUSES, tripId)) {
            throw new IllegalArgumentException("Xe này đang nằm trong chuyến chưa hoàn tất khác");
        }
    }

    private boolean isActiveTripStatus(TripStatus status) {
        return status == TripStatus.PLANNED || status == TripStatus.IN_PROGRESS;
    }

    private String normalizeText(String value) {
        return value == null ? null : value.trim();
    }

    private String normalizeNullableText(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}