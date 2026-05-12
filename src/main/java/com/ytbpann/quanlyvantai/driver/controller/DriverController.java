package com.ytbpann.quanlyvantai.driver.controller;

import com.ytbpann.quanlyvantai.driver.dto.DriverCreateRequest;
import com.ytbpann.quanlyvantai.driver.dto.DriverUpdateRequest;
import com.ytbpann.quanlyvantai.driver.entity.DriverProfile;
import com.ytbpann.quanlyvantai.driver.service.DriverService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/drivers")
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @GetMapping
    public String listDrivers(Model model) {
        model.addAttribute("pageTitle", "Danh sách tài xế");
        model.addAttribute("drivers", driverService.getAllDrivers());
        return "driver/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        if (!model.containsAttribute("driverCreateRequest")) {
            model.addAttribute("driverCreateRequest", new DriverCreateRequest());
        }

        populateCreateFormModel(model);
        return "driver/create";
    }

    @PostMapping("/create")
    public String createDriver(@ModelAttribute("driverCreateRequest") DriverCreateRequest request,
                               BindingResult bindingResult,
                               Model model,
                               RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            populateCreateFormModel(model);
            model.addAttribute("errorMessage", "Dữ liệu không hợp lệ. Vui lòng kiểm tra lại form.");
            return "driver/create";
        }

        try {
            driverService.createDriver(request);
            redirectAttributes.addFlashAttribute("successMessage", "Tạo hồ sơ tài xế thành công.");
            return "redirect:/admin/drivers";
        } catch (IllegalArgumentException ex) {
            populateCreateFormModel(model);
            model.addAttribute("errorMessage", ex.getMessage());
            return "driver/create";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        try {
            DriverProfile driverProfile = driverService.getDriverById(id);

            if (!model.containsAttribute("driverUpdateRequest")) {
                DriverUpdateRequest request = new DriverUpdateRequest();
                request.setDriverCode(driverProfile.getDriverCode());
                request.setFullName(driverProfile.getFullName());
                request.setPhoneNumber(driverProfile.getPhoneNumber());
                request.setLicenseNumber(driverProfile.getLicenseNumber());
                request.setLicenseExpiryDate(driverProfile.getLicenseExpiryDate());
                request.setAddress(driverProfile.getAddress());
                request.setNotes(driverProfile.getNotes());
                request.setActive(driverProfile.isActive());

                if (driverProfile.getLinkedUserAccount() != null) {
                    request.setLinkedUserAccountId(driverProfile.getLinkedUserAccount().getId());
                }

                model.addAttribute("driverUpdateRequest", request);
            }

            populateEditFormModel(model, id);
            return "driver/edit";
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/admin/drivers";
        }
    }

    @PostMapping("/{id}/edit")
    public String updateDriver(@PathVariable Long id,
                               @ModelAttribute("driverUpdateRequest") DriverUpdateRequest request,
                               BindingResult bindingResult,
                               Model model,
                               RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            populateEditFormModel(model, id);
            model.addAttribute("errorMessage", "Dữ liệu không hợp lệ. Vui lòng kiểm tra lại form.");
            return "driver/edit";
        }

        try {
            driverService.updateDriver(id, request);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật hồ sơ tài xế thành công.");
            return "redirect:/admin/drivers";
        } catch (IllegalArgumentException ex) {
            populateEditFormModel(model, id);
            model.addAttribute("errorMessage", ex.getMessage());
            return "driver/edit";
        }
    }

    @PostMapping("/{id}/toggle-status")
    public String toggleDriverStatus(@PathVariable Long id,
                                     RedirectAttributes redirectAttributes) {
        try {
            boolean active = driverService.toggleDriverStatus(id);
            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    active
                            ? "Đã bật trạng thái hoạt động cho tài xế."
                            : "Đã tắt trạng thái hoạt động của tài xế."
            );
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }

        return "redirect:/admin/drivers";
    }

    private void populateCreateFormModel(Model model) {
        model.addAttribute("pageTitle", "Tạo tài xế");
        model.addAttribute("availableDriverAccounts", driverService.getAvailableDriverAccounts());
    }

    private void populateEditFormModel(Model model, Long driverId) {
        model.addAttribute("pageTitle", "Cập nhật tài xế");
        model.addAttribute("driverId", driverId);
        model.addAttribute("availableDriverAccounts", driverService.getAvailableDriverAccountsForEdit(driverId));
    }
}