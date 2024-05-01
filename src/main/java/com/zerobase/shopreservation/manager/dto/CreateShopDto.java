package com.zerobase.shopreservation.manager.dto;

import com.zerobase.shopreservation.common.entity.Shop;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateShopDto {
    @NotBlank
    public String name;

    @NotBlank
    public String description;

    @NotBlank
    public String latitude;

    @NotBlank
    public String longitude;

    @NotBlank
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
