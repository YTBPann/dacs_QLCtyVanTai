package com.ytbpann.quanlyvantai.gps.controller;

import com.ytbpann.quanlyvantai.gps.dto.DriverGpsLocationRequest;
import com.ytbpann.quanlyvantai.gps.dto.DriverGpsLocationResponse;
import com.ytbpann.quanlyvantai.gps.service.GpsLocationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/driver/gps")
public class DriverGpsTrackingController {

    private final GpsLocationService gpsLocationService;

    public DriverGpsTrackingController(GpsLocationService gpsLocationService) {
        this.gpsLocationService = gpsLocationService;
    }

    @GetMapping
    public String showGpsTrackingPage() {
        return "driver/gps-tracking";
    }

    @PostMapping("/location")
    @ResponseBody
    public ResponseEntity<DriverGpsLocationResponse> updateLocation(
            @Valid @RequestBody DriverGpsLocationRequest request,
            Principal principal
    ) {
        if (principal == null) {
            return ResponseEntity.status(401)
                    .body(DriverGpsLocationResponse.error("Bạn chưa đăng nhập"));
        }

        try {
            DriverGpsLocationResponse response = gpsLocationService.saveDriverCurrentLocation(
                    principal.getName(),
                    request
            );

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest()
                    .body(DriverGpsLocationResponse.error(exception.getMessage()));
        }
    }
}