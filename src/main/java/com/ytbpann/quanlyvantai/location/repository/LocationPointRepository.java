package com.ytbpann.quanlyvantai.location.repository;

import com.ytbpann.quanlyvantai.location.entity.LocationPoint;
import com.ytbpann.quanlyvantai.location.entity.LocationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface LocationPointRepository extends JpaRepository<LocationPoint, Long> {

    List<LocationPoint> findAllByOrderByActiveDescCodeAscNameAsc();

    List<LocationPoint> findByActiveTrueOrderByCodeAscNameAsc();

    List<LocationPoint> findByActiveTrueAndTypeInOrderByCodeAscNameAsc(Collection<LocationType> types);

    boolean existsByCodeIgnoreCase(String code);

    boolean existsByCodeIgnoreCaseAndIdNot(String code, Long id);
}