package com.ytbpann.quanlyvantai.gps.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/gps")
public class AdminGpsMapController {

    @GetMapping("/map")
    public String showGpsMap() {
        return "gps/admin-map";
    }
}