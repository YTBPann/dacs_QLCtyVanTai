package com.ytbpann.quanlyvantai.location.repository;

import com.ytbpann.quanlyvantai.location.entity.LocationPoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationPointRepository extends JpaRepository<LocationPoint, Long> {

    List<LocationPoint> findAllByOrderByActiveDescCodeAscNameAsc();

    boolean existsByCodeIgnoreCase(String code);

    boolean existsByCodeIgnoreCaseAndIdNot(String code, Long id);
}