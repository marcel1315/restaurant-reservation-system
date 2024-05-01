package com.zerobase.shopreservation.customer.controller;

import com.zerobase.shopreservation.customer.dto.ShopDto;
import com.zerobase.shopreservation.customer.dto.ShopSearchDto;
import com.zerobase.shopreservation.customer.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("customerShopController")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;

    @GetMapping("/customer/shops")
    public ResponseEntity<?> list() {
        List<ShopDto> list = shopService.list();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/customer/shops/{id}")
    public ResponseEntity<?> detail(@PathVariable long id) {
        ShopDto shopDto = shopService.detail(id);
        return ResponseEntity.ok(shopDto);
    }

    @GetMapping("/customer/shops/search")
    public ResponseEntity<?> search(@ModelAttribute ShopSearchDto shopSearchDto) {
        List<ShopDto> list = shopService.search(shopSearchDto);
        return ResponseEntity.ok(list);
    }
}
