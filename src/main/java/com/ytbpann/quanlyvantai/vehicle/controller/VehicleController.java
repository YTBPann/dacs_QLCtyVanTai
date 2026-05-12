package com.ytbpann.quanlyvantai.vehicle.controller;

import com.ytbpann.quanlyvantai.vehicle.dto.VehicleCreateRequest;
import com.ytbpann.quanlyvantai.vehicle.dto.VehicleUpdateRequest;
import com.ytbpann.quanlyvantai.vehicle.entity.Vehicle;
import com.ytbpann.quanlyvantai.vehicle.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("vehicles", vehicleService.findAll());
        return "vehicle/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        if (!model.containsAttribute("vehicleCreateRequest")) {
            model.addAttribute("vehicleCreateRequest", new VehicleCreateRequest());
        }

        return "vehicle/create";
    }

    @PostMapping("/create")
    public String create(
            @Valid @ModelAttribute("vehicleCreateRequest") VehicleCreateRequest request,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            return "vehicle/create";
        }

        try {
            vehicleService.create(request);
            redirectAttributes.addFlashAttribute("successMessage", "Tạo xe mới thành công");
            return "redirect:/admin/vehicles";
        } catch (IllegalArgumentException exception) {
            bindingResult.rejectValue("licensePlate", "vehicle.licensePlate.duplicate", exception.getMessage());
            return "vehicle/create";
        }
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Vehicle vehicle = vehicleService.findById(id);

        if (!model.containsAttribute("vehicleUpdateRequest")) {
            VehicleUpdateRequest request = new VehicleUpdateRequest();
            request.setLicensePlate(vehicle.getLicensePlate());
            request.setVehicleType(vehicle.getVehicleType());
            request.setCapacity(vehicle.getCapacity());
            request.setActive(vehicle.isActive());
            request.setNotes(vehicle.getNotes());

            model.addAttribute("vehicleUpdateRequest", request);
        }

        model.addAttribute("vehicleId", vehicle.getId());
        return "vehicle/edit";
    }

    @PostMapping("/{id}/edit")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute("vehicleUpdateRequest") VehicleUpdateRequest request,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("vehicleId", id);
            return "vehicle/edit";
        }

        try {
            vehicleService.update(id, request);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật xe thành công");
            return "redirect:/admin/vehicles";
        } catch (IllegalArgumentException exception) {
            bindingResult.rejectValue("licensePlate", "vehicle.licensePlate.duplicate", exception.getMessage());
            model.addAttribute("vehicleId", id);
            return "vehicle/edit";
        }
    }

    @PostMapping("/{id}/toggle-active")
    public String toggleActive(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        vehicleService.toggleActive(id);
        redirectAttributes.addFlashAttribute("successMessage", "Đã cập nhật trạng thái xe");
        return "redirect:/admin/vehicles";
    }
}