package com.zerobase.shopreservation.manager.controller;

import com.zerobase.shopreservation.manager.dto.CreateShopDto;
import com.zerobase.shopreservation.manager.dto.ShopDto;
import com.zerobase.shopreservation.manager.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;

    @GetMapping("/manager/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(Collections.singletonMap("health", true));
    }

    @PostMapping("/manager/shops")
    public ResponseEntity<?> create(@Validated @RequestBody CreateShopDto createShopDto) {
        shopService.create(createShopDto);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/manager/shops")
    public ResponseEntity<?> list() {
        List<ShopDto> list = shopService.list();
        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/manager/shops/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        shopService.delete(id);
        return ResponseEntity.ok(null);
    }
}
