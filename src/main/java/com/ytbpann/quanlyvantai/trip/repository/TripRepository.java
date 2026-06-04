package com.ytbpann.quanlyvantai.trip.repository;

import com.ytbpann.quanlyvantai.trip.entity.Trip;
import com.ytbpann.quanlyvantai.trip.entity.TripStatus;
import com.ytbpann.quanlyvantai.user.entity.UserAccount;
import com.ytbpann.quanlyvantai.vehicle.entity.Vehicle;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface TripRepository extends JpaRepository<Trip, Long> {

    @EntityGraph(attributePaths = {"driver", "vehicle", "pickupLocation", "deliveryLocation"})
    List<Trip> findAllByOrderByIdAsc();

    @Override
    @EntityGraph(attributePaths = {"driver", "vehicle", "pickupLocation", "deliveryLocation"})
    Optional<Trip> findById(Long id);

    boolean existsByTripCodeIgnoreCase(String tripCode);

    boolean existsByTripCodeIgnoreCaseAndIdNot(String tripCode, Long id);

    Optional<Trip> findTopByTripCodeStartingWithOrderByTripCodeDesc(String prefix);

    boolean existsByDriverAndStatusIn(UserAccount driver, Collection<TripStatus> statuses);

    boolean existsByDriverAndStatusInAndIdNot(UserAccount driver, Collection<TripStatus> statuses, Long id);

    boolean existsByVehicleAndStatusIn(Vehicle vehicle, Collection<TripStatus> statuses);

    boolean existsByVehicleAndStatusInAndIdNot(Vehicle vehicle, Collection<TripStatus> statuses, Long id);

    @EntityGraph(attributePaths = {"driver", "vehicle", "pickupLocation", "deliveryLocation"})
    Optional<Trip> findTopByDriverAndStatusOrderByIdDesc(UserAccount driver, TripStatus status);
}