package com.zerobase.restaurantreservationsystem.common.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/health")
    public String health() {
        return "healthy";
    }

    @GetMapping("/authorized/health")
    public String authorized() {
        return "healthy";
    }
}
