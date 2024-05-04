package com.zerobase.shopreservation.common.dto;

import com.zerobase.shopreservation.common.entity.Shop;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShopOutputDto {
    private long id;
    private String name;
    private String address;
    private String longitude;
    private String latitude;
    private String description;
    private String distanceInKm;

    public static ShopOutputDto of(Shop shop) {
        return ShopOutputDto.builder()
                .id(shop.getId())
                .name(shop.getName())
                .address(shop.getAddress())
                .longitude(shop.getLongitude())
                .latitude(shop.getLatitude())
                .description(shop.getDescription())
                .distanceInKm(null)
                .build();
    }
}
