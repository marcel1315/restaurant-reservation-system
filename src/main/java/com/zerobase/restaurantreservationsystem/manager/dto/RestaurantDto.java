package com.zerobase.restaurantreservationsystem.manager.dto;

import com.zerobase.restaurantreservationsystem.common.entity.Restaurant;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RestaurantDto {
    String name;
    String description;
    String address;
    String latitude;
    String longitude;

    public static RestaurantDto of(Restaurant restaurant) {
        return RestaurantDto.builder()
                .name(restaurant.getName())
                .description(restaurant.getDescription())
                .address(restaurant.getAddress())
                .latitude(restaurant.getLatitude())
                .longitude(restaurant.getLongitude())
                .build();
    }
}
