package com.ytbpann.quanlyvantai.trip.controller;

import com.ytbpann.quanlyvantai.trip.dto.TripForm;
import com.ytbpann.quanlyvantai.trip.entity.Trip;
import com.ytbpann.quanlyvantai.trip.entity.TripStatus;
import com.ytbpann.quanlyvantai.trip.service.TripService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/trips")
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @GetMapping
    public String listTrips(Model model) {
        model.addAttribute("trips", tripService.findAllTrips());
        model.addAttribute("statuses", TripStatus.values());
        return "trip/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("tripForm", new TripForm());
        addFormAttributesForCreate(model);
        return "trip/form";
    }

    @PostMapping("/create")
    public String createTrip(
            @Valid @ModelAttribute("tripForm") TripForm tripForm,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            addFormAttributesForCreate(model);
            return "trip/form";
        }

        try {
            tripService.createTrip(tripForm);
            redirectAttributes.addFlashAttribute("successMessage", "Đã tạo chuyến thành công");
            return "redirect:/admin/trips";
        } catch (IllegalArgumentException ex) {
            bindingResult.reject("tripCreateError", ex.getMessage());
            addFormAttributesForCreate(model);
            return "trip/form";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(
            @PathVariable Long id,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        try {
            Trip trip = tripService.findById(id);
            model.addAttribute("tripForm", tripService.toForm(trip));
            addFormAttributesForEdit(model, id);
            return "trip/form";
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/admin/trips";
        }
    }

    @PostMapping("/{id}/edit")
    public String updateTrip(
            @PathVariable Long id,
            @Valid @ModelAttribute("tripForm") TripForm tripForm,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            addFormAttributesForEdit(model, id);
            return "trip/form";
        }

        try {
            tripService.updateTrip(id, tripForm);
            redirectAttributes.addFlashAttribute("successMessage", "Đã cập nhật chuyến thành công");
            return "redirect:/admin/trips";
        } catch (IllegalArgumentException ex) {
            bindingResult.reject("tripUpdateError", ex.getMessage());
            addFormAttributesForEdit(model, id);
            return "trip/form";
        }
    }

    @GetMapping("/{id}/route")
    public String showRoutePage(
            @PathVariable Long id,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        try {
            Trip trip = tripService.findById(id);
            model.addAttribute("trip", trip);
            return "trip/route";
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/admin/trips";
        }
    }

    @PostMapping("/{id}/status")
    public String updateStatus(
            @PathVariable Long id,
            @RequestParam TripStatus status,
            RedirectAttributes redirectAttributes
    ) {
        try {
            tripService.updateStatus(id, status);
            redirectAttributes.addFlashAttribute("successMessage", "Đã cập nhật trạng thái chuyến");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }

        return "redirect:/admin/trips";
    }

    private void addFormAttributesForCreate(Model model) {
        model.addAttribute("formTitle", "Tạo chuyến mới");
        model.addAttribute("mode", "create");
        model.addAttribute("tripId", null);
        model.addAttribute("formAction", "/admin/trips/create");
        model.addAttribute("statuses", TripStatus.values());
        model.addAttribute("drivers", tripService.findAvailableDriversForCreate());
        model.addAttribute("vehicles", tripService.findAvailableVehiclesForCreate());
        model.addAttribute("pickupLocations", tripService.findActivePickupLocations());
        model.addAttribute("deliveryLocations", tripService.findActiveDeliveryLocations());
    }

    private void addFormAttributesForEdit(Model model, Long tripId) {
        model.addAttribute("formTitle", "Sửa chuyến");
        model.addAttribute("mode", "edit");
        model.addAttribute("tripId", tripId);
        model.addAttribute("formAction", "/admin/trips/" + tripId + "/edit");
        model.addAttribute("statuses", TripStatus.values());
        model.addAttribute("drivers", tripService.findAvailableDriversForEdit(tripId));
        model.addAttribute("vehicles", tripService.findAvailableVehiclesForEdit(tripId));
        model.addAttribute("pickupLocations", tripService.findActivePickupLocations());
        model.addAttribute("deliveryLocations", tripService.findActiveDeliveryLocations());
    }
}