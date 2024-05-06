package com.zerobase.shopreservation.manager.controller;

import com.zerobase.shopreservation.manager.dto.CreateShopDto;
import com.zerobase.shopreservation.manager.dto.ShopOutputDto;
import com.zerobase.shopreservation.manager.dto.UpdateShopDto;
import com.zerobase.shopreservation.manager.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController("managerShopController")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;

    /**
     * 매니저가 상점을 생성
     */
    @PostMapping("/manager/shops")
    public ResponseEntity<?> create(@Validated @RequestBody CreateShopDto createShopDto) {
        shopService.create(createShopDto);
        return ResponseEntity.ok(null);
    }

    /**
     * 매니저가 상점의 정보를 업데이트
     */
    @PostMapping("/manager/shops/{id}")
    public ResponseEntity<?> update(@Validated @RequestBody UpdateShopDto updateShopDto, @PathVariable long id) {
        shopService.update(id, updateShopDto);
        return ResponseEntity.ok(null);
    }

    /**
     * 매니저의 모든 상점 목록 보기
     */
    @GetMapping("/manager/shops")
    public ResponseEntity<?> list() {
        List<ShopOutputDto> list = shopService.list();
        return ResponseEntity.ok(list);
    }

    /**
     * 매니저가 상점을 제거
     * 실제 DB에서 제거하지 않고, delete marker를 남김
     */
    @DeleteMapping("/manager/shops/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        shopService.delete(id);
        return ResponseEntity.ok(null);
    }
}
