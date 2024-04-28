package com.zerobase.restaurantreservationsystem.customer.controller;

import com.zerobase.restaurantreservationsystem.customer.dto.RestaurantDto;
import com.zerobase.restaurantreservationsystem.customer.dto.RestaurantSearchDto;
import com.zerobase.restaurantreservationsystem.customer.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("customerRestaurantController")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @GetMapping("/customer/restaurants")
    public ResponseEntity<?> list() {
        List<RestaurantDto> list = restaurantService.list();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/customer/restaurants/{id}")
    public ResponseEntity<?> detail(@PathVariable long id) {
        RestaurantDto restaurant = restaurantService.detail(id);
        return ResponseEntity.ok(restaurant);
    }

    @GetMapping("/customer/restaurants/search")
    public ResponseEntity<?> search(@ModelAttribute RestaurantSearchDto restaurantSearchDto) {
        List<RestaurantDto> list = restaurantService.search(restaurantSearchDto);
        return ResponseEntity.ok(list);
    }
}
