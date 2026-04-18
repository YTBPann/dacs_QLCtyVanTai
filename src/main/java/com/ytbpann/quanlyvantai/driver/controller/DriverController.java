package com.ytbpann.quanlyvantai.driver.controller;

import com.ytbpann.quanlyvantai.driver.dto.DriverCreateRequest;
import com.ytbpann.quanlyvantai.driver.service.DriverService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
        model.addAttribute("pageTitle", "Tạo tài xế");
        return "driver/create";
    }

    @PostMapping("/create")
    public String createDriver(@ModelAttribute("driverCreateRequest") DriverCreateRequest request,
                               RedirectAttributes redirectAttributes) {
        try {
            driverService.createDriver(request);
            redirectAttributes.addFlashAttribute("successMessage", "Tạo hồ sơ tài xế thành công.");
            return "redirect:/admin/drivers";
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            redirectAttributes.addFlashAttribute("driverCreateRequest", request);
            return "redirect:/admin/drivers/create";
        }
    }
}