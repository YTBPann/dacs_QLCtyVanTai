package com.ytbpann.quanlyvantai.gps.controller;

import com.ytbpann.quanlyvantai.gps.dto.GpsLocationResponse;
import com.ytbpann.quanlyvantai.gps.dto.GpsLocationUpdateRequest;
import com.ytbpann.quanlyvantai.gps.service.GpsLocationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ytbpann.quanlyvantai.gps.dto.VehicleGpsMarkerResponse;

import java.util.List;

@RestController
@RequestMapping("/api/gps")
public class GpsLocationApiController {

    private final GpsLocationService gpsLocationService;

    public GpsLocationApiController(GpsLocationService gpsLocationService) {
        this.gpsLocationService = gpsLocationService;
    }

    @PostMapping("/location")
    public ResponseEntity<GpsLocationResponse> saveLocation(
            @Valid @RequestBody GpsLocationUpdateRequest request
    ) {
        GpsLocationResponse response = gpsLocationService.saveLocation(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/latest/drivers")
    public ResponseEntity<List<GpsLocationResponse>> getLatestPerDriver() {
        return ResponseEntity.ok(gpsLocationService.getLatestPerDriver());
    }

    @GetMapping("/latest/vehicles")
    public ResponseEntity<List<GpsLocationResponse>> getLatestPerVehicle() {
        return ResponseEntity.ok(gpsLocationService.getLatestPerVehicle());
    }

    @GetMapping("/latest/vehicle-markers")
    public ResponseEntity<List<VehicleGpsMarkerResponse>> getLatestVehicleMarkers() {
        return ResponseEntity.ok(gpsLocationService.getLatestVehicleMarkers());
    }

    @GetMapping("/trips/{tripId}/history")
    public ResponseEntity<List<GpsLocationResponse>> getTripHistory(
            @PathVariable Long tripId
    ) {
        return ResponseEntity.ok(gpsLocationService.getTripHistory(tripId));
    }
}