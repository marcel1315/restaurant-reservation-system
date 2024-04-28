package com.zerobase.shopreservation.customer.dto;

import com.zerobase.shopreservation.common.entity.Shop;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShopDto {
    private long id;
    private String name;
    private String address;
    private String longitude;
    private String latitude;

    public static ShopDto of(Shop shop) {
        return ShopDto.builder()
                .id(shop.getId())
                .name(shop.getName())
                .address(shop.getAddress())
                .longitude(shop.getLongitude())
                .latitude(shop.getLatitude())
                .build();
    }
}
