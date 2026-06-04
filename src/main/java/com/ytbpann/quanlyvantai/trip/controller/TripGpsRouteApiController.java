package com.ytbpann.quanlyvantai.trip.controller;

import com.ytbpann.quanlyvantai.trip.dto.TripGpsRoutePointResponse;
import com.ytbpann.quanlyvantai.trip.service.TripGpsRouteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/trips")
public class TripGpsRouteApiController {

    private final TripGpsRouteService tripGpsRouteService;

    public TripGpsRouteApiController(TripGpsRouteService tripGpsRouteService) {
        this.tripGpsRouteService = tripGpsRouteService;
    }

    @GetMapping("/{tripId}/gps-route")
    public List<TripGpsRoutePointResponse> getGpsRoute(@PathVariable Long tripId) {
        return tripGpsRouteService.findRoutePointsByTripId(tripId);
    }
}