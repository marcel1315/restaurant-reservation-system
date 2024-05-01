package com.zerobase.shopreservation.manager.dto;

import com.zerobase.shopreservation.common.entity.Shop;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShopOutputDto {
    String name;
    String description;
    String address;
    String latitude;
    String longitude;

    public static ShopOutputDto of(Shop shop) {
        return ShopOutputDto.builder()
                .name(shop.getName())
                .description(shop.getDescription())
                .address(shop.getAddress())
                .latitude(shop.getLatitude())
                .longitude(shop.getLongitude())
                .build();
    }
}
