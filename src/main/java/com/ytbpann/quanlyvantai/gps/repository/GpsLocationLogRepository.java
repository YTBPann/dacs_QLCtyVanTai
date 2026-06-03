package com.ytbpann.quanlyvantai.gps.repository;

import com.ytbpann.quanlyvantai.gps.entity.GpsLocationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GpsLocationLogRepository extends JpaRepository<GpsLocationLog, Long> {

    Optional<GpsLocationLog> findTopByTripIdOrderByRecordedAtDesc(Long tripId);

    Optional<GpsLocationLog> findTopByDriverIdOrderByRecordedAtDesc(Long driverId);

    Optional<GpsLocationLog> findTopByVehicleIdOrderByRecordedAtDesc(Long vehicleId);

    List<GpsLocationLog> findTop100ByTripIdOrderByRecordedAtDesc(Long tripId);

    @Query("""
            select g
            from GpsLocationLog g
            where g.id in (
                select max(g2.id)
                from GpsLocationLog g2
                where g2.driver is not null
                group by g2.driver.id
            )
            order by g.recordedAt desc
            """)
    List<GpsLocationLog> findLatestPerDriver();

    @Query("""
            select g
            from GpsLocationLog g
            where g.id in (
                select max(g2.id)
                from GpsLocationLog g2
                where g2.vehicle is not null
                group by g2.vehicle.id
            )
            order by g.recordedAt desc
            """)
    List<GpsLocationLog> findLatestPerVehicle();
}