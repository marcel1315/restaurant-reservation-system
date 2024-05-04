package com.zerobase.shopreservation.customer.controller;

import com.zerobase.shopreservation.common.dto.ShopOutputDto;
import com.zerobase.shopreservation.common.dto.ShopOutputPageDto;
import com.zerobase.shopreservation.customer.dto.OneShopSearchDto;
import com.zerobase.shopreservation.customer.dto.ShopSearchDto;
import com.zerobase.shopreservation.customer.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController("customerShopController")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;

    @GetMapping("/customer/shops/{id}")
    public ResponseEntity<?> detail(@PathVariable long id, @ModelAttribute OneShopSearchDto dto) {
        dto.setId(id);
        ShopOutputDto shopOutputDto = shopService.detail(dto);
        return ResponseEntity.ok(shopOutputDto);
    }

    @GetMapping("/customer/shops")
    public ResponseEntity<?> search(@Validated @ModelAttribute ShopSearchDto shopSearchDto) {
        ShopOutputPageDto page = shopService.search(shopSearchDto);
        return ResponseEntity.ok(page);
    }
}
