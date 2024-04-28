package com.zerobase.restaurantreservationsystem.manager.dto;

import com.zerobase.restaurantreservationsystem.common.entity.Restaurant;
import lombok.Data;

@Data
public class CreateRestaurantDto {
    public String name;
    public String description;
    public String latitude;
    public String longitude;
    public String address;

    public Restaurant toRestaurant() {
        return Restaurant.builder()
                .name(getName())
                .address(getAddress())
                .description(getDescription())
                .latitude(getLatitude())
                .longitude(getLongitude())
                .build();
    }
}
