package com.zerobase.shopreservation.customer.controller;

import com.zerobase.shopreservation.customer.dto.OneShopSearchDto;
import com.zerobase.shopreservation.customer.dto.ShopOutputDto;
import com.zerobase.shopreservation.customer.dto.ShopOutputPageDto;
import com.zerobase.shopreservation.customer.dto.ShopSearchDto;
import com.zerobase.shopreservation.customer.service.ShopService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController("customerShopController")
@RequiredArgsConstructor
@Tag(name = "5 - Customer Shop", description = "고객 상점 관련")
public class ShopController {

    private final ShopService shopService;

    @Operation(
            summary = "상점의 자세한 정보 보기"
    )
    @GetMapping("/customer/shops/{id}")
    public ResponseEntity<?> detail(@PathVariable long id, @ModelAttribute OneShopSearchDto dto) {
        dto.setId(id);
        ShopOutputDto shopOutputDto = shopService.detail(dto);
        return ResponseEntity.ok(shopOutputDto);
    }

    @Operation(
            summary = "상점 목록 보기"
    )
    @GetMapping("/customer/shops")
    public ResponseEntity<?> search(@Validated @ModelAttribute ShopSearchDto shopSearchDto) {
        ShopOutputPageDto page = shopService.search(shopSearchDto);
        return ResponseEntity.ok(page);
    }
}
