package com.ytbpann.quanlyvantai.location.controller;

import com.ytbpann.quanlyvantai.location.dto.LocationForm;
import com.ytbpann.quanlyvantai.location.entity.LocationPoint;
import com.ytbpann.quanlyvantai.location.entity.LocationType;
import com.ytbpann.quanlyvantai.location.service.LocationManagementService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/locations")
public class LocationManagementController {

    private final LocationManagementService locationManagementService;

    public LocationManagementController(LocationManagementService locationManagementService) {
        this.locationManagementService = locationManagementService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("locations", locationManagementService.findAll());
        return "location/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("locationForm", new LocationForm());
        model.addAttribute("locationTypes", LocationType.values());
        model.addAttribute("isEdit", false);
        model.addAttribute("locationId", null);
        model.addAttribute("pageTitle", "Tạo địa điểm mới");
        return "location/form";
    }

    @PostMapping("/create")
    public String create(
            @Valid @ModelAttribute("locationForm") LocationForm locationForm,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("locationTypes", LocationType.values());
            model.addAttribute("isEdit", false);
            model.addAttribute("locationId", null);
            model.addAttribute("pageTitle", "Tạo địa điểm mới");
            return "location/form";
        }

        try {
            locationManagementService.create(locationForm);
            redirectAttributes.addFlashAttribute("successMessage", "Đã tạo địa điểm mới thành công.");
            return "redirect:/admin/locations";
        } catch (IllegalArgumentException exception) {
            bindingResult.rejectValue("code", "duplicate", exception.getMessage());
            model.addAttribute("locationTypes", LocationType.values());
            model.addAttribute("isEdit", false);
            model.addAttribute("locationId", null);
            model.addAttribute("pageTitle", "Tạo địa điểm mới");
            return "location/form";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        LocationPoint locationPoint = locationManagementService.findById(id);

        model.addAttribute("locationForm", LocationForm.fromEntity(locationPoint));
        model.addAttribute("locationTypes", LocationType.values());
        model.addAttribute("isEdit", true);
        model.addAttribute("locationId", id);
        model.addAttribute("pageTitle", "Sửa địa điểm");
        return "location/form";
    }

    @PostMapping("/{id}/edit")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute("locationForm") LocationForm locationForm,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("locationTypes", LocationType.values());
            model.addAttribute("isEdit", true);
            model.addAttribute("locationId", id);
            model.addAttribute("pageTitle", "Sửa địa điểm");
            return "location/form";
        }

        try {
            locationManagementService.update(id, locationForm);
            redirectAttributes.addFlashAttribute("successMessage", "Đã cập nhật địa điểm thành công.");
            return "redirect:/admin/locations";
        } catch (IllegalArgumentException exception) {
            bindingResult.rejectValue("code", "duplicate", exception.getMessage());
            model.addAttribute("locationTypes", LocationType.values());
            model.addAttribute("isEdit", true);
            model.addAttribute("locationId", id);
            model.addAttribute("pageTitle", "Sửa địa điểm");
            return "location/form";
        }
    }

    @PostMapping("/{id}/toggle")
    public String toggleActive(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        locationManagementService.toggleActive(id);
        redirectAttributes.addFlashAttribute("successMessage", "Đã cập nhật trạng thái địa điểm.");
        return "redirect:/admin/locations";
    }
}