package com.ytbpann.quanlyvantai.trip.service;

import com.ytbpann.quanlyvantai.location.entity.LocationPoint;
import com.ytbpann.quanlyvantai.location.entity.LocationType;
import com.ytbpann.quanlyvantai.location.repository.LocationPointRepository;
import com.ytbpann.quanlyvantai.location.service.LocationManagementService;
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class TripService {

    private static final DateTimeFormatter TRIP_CODE_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final TripRepository tripRepository;
    private final UserAccountRepository userAccountRepository;
    private final VehicleRepository vehicleRepository;
    private final LocationPointRepository locationPointRepository;
    private final LocationManagementService locationManagementService;

    public TripService(
            TripRepository tripRepository,
            UserAccountRepository userAccountRepository,
            VehicleRepository vehicleRepository,
            LocationPointRepository locationPointRepository,
            LocationManagementService locationManagementService
    ) {
        this.tripRepository = tripRepository;
        this.userAccountRepository = userAccountRepository;
        this.vehicleRepository = vehicleRepository;
        this.locationPointRepository = locationPointRepository;
        this.locationManagementService = locationManagementService;
    }

    @Transactional(readOnly = true)
    public List<Trip> findAllTrips() {
        return tripRepository.findAllByOrderByIdAsc();
    }

    @Transactional(readOnly = true)
    public Trip findById(Long id) {
        return tripRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy chuyến có ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<UserAccount> findAvailableDriversForCreate() {
        return userAccountRepository.findByRoleAndEnabledTrueOrderByFullNameAsc(RoleName.DRIVER);
    }

    @Transactional(readOnly = true)
    public List<Vehicle> findAvailableVehiclesForCreate() {
        return vehicleRepository.findByActiveTrueOrderByLicensePlateAsc();
    }

    @Transactional(readOnly = true)
    public List<UserAccount> findAvailableDriversForEdit(Long tripId) {
        return userAccountRepository.findByRoleAndEnabledTrueOrderByFullNameAsc(RoleName.DRIVER);
    }

    @Transactional(readOnly = true)
    public List<Vehicle> findAvailableVehiclesForEdit(Long tripId) {
        return vehicleRepository.findByActiveTrueOrderByLicensePlateAsc();
    }

    @Transactional(readOnly = true)
    public List<LocationPoint> findActivePickupLocations() {
        return locationManagementService.findActivePickupLocations();
    }

    @Transactional(readOnly = true)
    public List<LocationPoint> findActiveDeliveryLocations() {
        return locationManagementService.findActiveDeliveryLocations();
    }

    public Trip createTrip(TripForm form) {
        validateTimeRange(form);

        UserAccount driver = findActiveDriver(form.getDriverId());
        Vehicle vehicle = findActiveVehicle(form.getVehicleId());
        LocationPoint pickupLocation = findActivePickupLocation(form.getPickupLocationId());
        LocationPoint deliveryLocation = findActiveDeliveryLocation(form.getDeliveryLocationId());

        TripStatus status = form.getStatus() == null ? TripStatus.PLANNED : form.getStatus();
        String tripCode = generateTripCode(pickupLocation, deliveryLocation, form.getPlannedStartTime());

        Trip trip = new Trip();
        applyFormToTrip(trip, form, driver, vehicle, pickupLocation, deliveryLocation, status, tripCode);

        return tripRepository.save(trip);
    }

    public Trip updateTrip(Long id, TripForm form) {
        Trip trip = findById(id);

        validateTimeRange(form);

        UserAccount driver = findActiveDriver(form.getDriverId());
        Vehicle vehicle = findActiveVehicle(form.getVehicleId());
        LocationPoint pickupLocation = findActivePickupLocation(form.getPickupLocationId());
        LocationPoint deliveryLocation = findActiveDeliveryLocation(form.getDeliveryLocationId());

        TripStatus status = form.getStatus() == null ? TripStatus.PLANNED : form.getStatus();
        String tripCode = resolveTripCodeForUpdate(trip, pickupLocation, deliveryLocation, form);

        applyFormToTrip(trip, form, driver, vehicle, pickupLocation, deliveryLocation, status, tripCode);

        return tripRepository.save(trip);
    }

    public Trip updateStatus(Long id, TripStatus status) {
        Trip trip = findById(id);

        if (status == null) {
            throw new IllegalArgumentException("Trạng thái chuyến không hợp lệ");
        }

        trip.setStatus(status);
        return tripRepository.save(trip);
    }

    public TripForm toForm(Trip trip) {
        TripForm form = new TripForm();

        form.setTripCode(trip.getTripCode());
        form.setDriverId(trip.getDriver().getId());
        form.setVehicleId(trip.getVehicle().getId());

        if (trip.getPickupLocation() != null) {
            form.setPickupLocationId(trip.getPickupLocation().getId());
        }

        if (trip.getDeliveryLocation() != null) {
            form.setDeliveryLocationId(trip.getDeliveryLocation().getId());
        }

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
            LocationPoint pickupLocation,
            LocationPoint deliveryLocation,
            TripStatus status,
            String tripCode
    ) {
        trip.setTripCode(normalizeText(tripCode));
        trip.setDriver(driver);
        trip.setVehicle(vehicle);

        trip.setPickupLocation(pickupLocation);
        trip.setDeliveryLocation(deliveryLocation);

        /*
         * Vẫn điền 2 cột text cũ để:
         * - không lỗi NOT NULL trong database cũ
         * - dữ liệu Trip Phase 1 không bị phá
         * - template/list cũ vẫn còn fallback được nếu cần
         */
        trip.setDeparturePoint(buildLocationDisplayName(pickupLocation));
        trip.setDestinationPoint(buildLocationDisplayName(deliveryLocation));

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

    private LocationPoint findActivePickupLocation(Long locationId) {
        LocationPoint location = findActiveLocation(locationId, "điểm lấy hàng");

        if (!isPickupLocationType(location.getType())) {
            throw new IllegalArgumentException("Địa điểm được chọn không phải điểm lấy hàng hợp lệ");
        }

        return location;
    }

    private LocationPoint findActiveDeliveryLocation(Long locationId) {
        LocationPoint location = findActiveLocation(locationId, "điểm giao hàng");

        if (!isDeliveryLocationType(location.getType())) {
            throw new IllegalArgumentException("Địa điểm được chọn không phải điểm giao hàng hợp lệ");
        }

        return location;
    }

    private LocationPoint findActiveLocation(Long locationId, String label) {
        if (locationId == null) {
            throw new IllegalArgumentException("Vui lòng chọn " + label);
        }

        LocationPoint location = locationPointRepository.findById(locationId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy " + label));

        if (!location.isActive()) {
            throw new IllegalArgumentException("Địa điểm được chọn đang bị tắt");
        }

        if (location.getType() == null) {
            throw new IllegalArgumentException("Địa điểm được chọn chưa có loại hợp lệ");
        }

        return location;
    }

    private boolean isPickupLocationType(LocationType type) {
        return type == LocationType.PICKUP_POINT || type == LocationType.BOTH;
    }

    private boolean isDeliveryLocationType(LocationType type) {
        return type == LocationType.DELIVERY_POINT || type == LocationType.BOTH;
    }

    private String resolveTripCodeForUpdate(
            Trip trip,
            LocationPoint pickupLocation,
            LocationPoint deliveryLocation,
            TripForm form
    ) {
        if (isBlank(trip.getTripCode())) {
            return generateTripCode(pickupLocation, deliveryLocation, form.getPlannedStartTime());
        }

        if (shouldRegenerateTripCode(trip, pickupLocation, deliveryLocation, form)) {
            return generateTripCode(pickupLocation, deliveryLocation, form.getPlannedStartTime());
        }

        return trip.getTripCode();
    }

    private boolean shouldRegenerateTripCode(
            Trip trip,
            LocationPoint pickupLocation,
            LocationPoint deliveryLocation,
            TripForm form
    ) {
        Long oldPickupId = trip.getPickupLocation() == null ? null : trip.getPickupLocation().getId();
        Long oldDeliveryId = trip.getDeliveryLocation() == null ? null : trip.getDeliveryLocation().getId();

        Long newPickupId = pickupLocation == null ? null : pickupLocation.getId();
        Long newDeliveryId = deliveryLocation == null ? null : deliveryLocation.getId();

        LocalDate oldDate = trip.getPlannedStartTime() == null ? null : trip.getPlannedStartTime().toLocalDate();
        LocalDate newDate = form.getPlannedStartTime() == null ? null : form.getPlannedStartTime().toLocalDate();

        return !Objects.equals(oldPickupId, newPickupId)
                || !Objects.equals(oldDeliveryId, newDeliveryId)
                || !Objects.equals(oldDate, newDate);
    }

    private String generateTripCode(
            LocationPoint pickupLocation,
            LocationPoint deliveryLocation,
            java.time.LocalDateTime plannedStartTime
    ) {
        String pickupCode = normalizeLocationCode(pickupLocation.getCode());
        String deliveryCode = normalizeLocationCode(deliveryLocation.getCode());

        LocalDate tripDate = plannedStartTime == null
                ? LocalDate.now()
                : plannedStartTime.toLocalDate();

        String datePart = tripDate.format(TRIP_CODE_DATE_FORMATTER);
        String prefix = pickupCode + "-" + deliveryCode + "-" + datePart + "-";

        int nextSequence = tripRepository.findTopByTripCodeStartingWithOrderByTripCodeDesc(prefix)
                .map(latestTrip -> extractSequence(latestTrip.getTripCode(), prefix))
                .orElse(0) + 1;

        return prefix + String.format("%03d", nextSequence);
    }

    private int extractSequence(String tripCode, String prefix) {
        if (tripCode == null || !tripCode.startsWith(prefix)) {
            return 0;
        }

        String sequenceText = tripCode.substring(prefix.length());

        try {
            return Integer.parseInt(sequenceText);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    private String normalizeLocationCode(String code) {
        String normalizedCode = normalizeText(code);

        if (isBlank(normalizedCode)) {
            throw new IllegalArgumentException("Mã địa điểm không hợp lệ, không thể tự sinh mã chuyến");
        }

        return normalizedCode;
    }

    private String buildLocationDisplayName(LocationPoint location) {
        String code = normalizeNullableText(location.getCode());
        String name = normalizeNullableText(location.getName());

        if (code == null && name == null) {
            return "Không rõ địa điểm";
        }

        if (code == null) {
            return name;
        }

        if (name == null) {
            return code;
        }

        return code + " - " + name;
    }

    private void validateTimeRange(TripForm form) {
        if (form.getPlannedStartTime() == null || form.getPlannedEndTime() == null) {
            return;
        }

        if (!form.getPlannedEndTime().isAfter(form.getPlannedStartTime())) {
            throw new IllegalArgumentException("Thời gian kết thúc phải sau thời gian bắt đầu");
        }
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

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}