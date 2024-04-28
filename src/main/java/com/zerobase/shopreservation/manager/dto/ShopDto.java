package com.zerobase.shopreservation.manager.dto;

import com.zerobase.shopreservation.common.entity.Shop;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShopDto {
    String name;
    String description;
    String address;
    String latitude;
    String longitude;

    public static ShopDto of(Shop shop) {
        return ShopDto.builder()
                .name(shop.getName())
                .description(shop.getDescription())
                .address(shop.getAddress())
                .latitude(shop.getLatitude())
                .longitude(shop.getLongitude())
                .build();
    }
}
