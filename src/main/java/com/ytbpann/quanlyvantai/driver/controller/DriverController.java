package com.ytbpann.quanlyvantai.driver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/drivers")
public class DriverController {

    @GetMapping
    public String listDrivers(Model model) {
        model.addAttribute("pageTitle", "Danh sách tài xế");
        model.addAttribute("drivers", List.of());
        return "driver/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("pageTitle", "Tạo tài xế");
        return "driver/create";
    }
}