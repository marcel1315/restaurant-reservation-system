package com.zerobase.shopreservation.manager.dto;

import com.zerobase.shopreservation.common.entity.Shop;
import lombok.Data;

@Data
public class CreateShopDto {
    public String name;
    public String description;
    public String latitude;
    public String longitude;
    public String address;

    public Shop toShop() {
        return Shop.builder()
                .name(getName())
                .address(getAddress())
                .description(getDescription())
                .latitude(getLatitude())
                .longitude(getLongitude())
                .build();
    }
}
