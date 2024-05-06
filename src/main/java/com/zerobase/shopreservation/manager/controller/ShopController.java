package com.zerobase.shopreservation.manager.controller;

import com.zerobase.shopreservation.manager.dto.CreateShopDto;
import com.zerobase.shopreservation.manager.dto.ShopOutputDto;
import com.zerobase.shopreservation.manager.dto.UpdateShopDto;
import com.zerobase.shopreservation.manager.service.ShopService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("managerShopController")
@RequiredArgsConstructor
@Tag(name = "2 - Manager Shop", description = "매니저 상점 관련")
public class ShopController {

    private final ShopService shopService;

    @Operation(
            summary = "매니저가 상점을 생성"
    )
    @PostMapping("/manager/shops")
    public ResponseEntity<?> create(@Validated @RequestBody CreateShopDto createShopDto) {
        shopService.create(createShopDto);
        return ResponseEntity.ok(null);
    }

    @Operation(
            summary = "매니저가 상점의 정보를 업데이트"
    )
    @PostMapping("/manager/shops/{id}")
    public ResponseEntity<?> update(@Validated @RequestBody UpdateShopDto updateShopDto, @PathVariable long id) {
        shopService.update(id, updateShopDto);
        return ResponseEntity.ok(null);
    }

    @Operation(
            summary = "매니저의 모든 상점 목록 보기"
    )
    @GetMapping("/manager/shops")
    public ResponseEntity<?> list() {
        List<ShopOutputDto> list = shopService.list();
        return ResponseEntity.ok(list);
    }

    @Operation(
            summary = "매니저가 상점을 제거",
            description = "실제 DB에서 제거하지 않고, delete marker를 남김"
    )
    @DeleteMapping("/manager/shops/{id}")
    public ResponseEntity<?> delete(
            @PathVariable
            @Parameter(
                    schema = @Schema(defaultValue = "1")
            )
            long id) {
        shopService.delete(id);
        return ResponseEntity.ok(null);
    }
}
