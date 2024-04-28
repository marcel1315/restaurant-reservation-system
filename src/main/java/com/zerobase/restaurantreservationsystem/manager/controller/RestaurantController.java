package com.zerobase.restaurantreservationsystem.manager.controller;

import com.zerobase.restaurantreservationsystem.manager.dto.CreateRestaurantDto;
import com.zerobase.restaurantreservationsystem.manager.dto.RestaurantDto;
import com.zerobase.restaurantreservationsystem.manager.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @GetMapping("/manager/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(Collections.singletonMap("health", true));
    }

    @PostMapping("/manager/restaurants")
    public ResponseEntity<?> create(@RequestBody CreateRestaurantDto createRestaurantDto) {
        restaurantService.create(createRestaurantDto);
        return ResponseEntity.ok(Collections.singletonMap("success", true));
    }

    @GetMapping("/manager/restaurants")
    public ResponseEntity<?> list() {
        List<RestaurantDto> list = restaurantService.list();
        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/manager/restaurants/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        restaurantService.delete(id);
        return ResponseEntity.ok(Collections.singletonMap("success", true));
    }
}
