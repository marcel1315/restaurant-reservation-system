package com.zerobase.restaurantreservationsystem.customer.dto;

import com.zerobase.restaurantreservationsystem.common.entity.Restaurant;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RestaurantDto {
    private String name;
    private String address;
    private String longitude;
    private String latitude;

    public static RestaurantDto of(Restaurant restaurant) {
        return RestaurantDto.builder()
                .name(restaurant.getName())
                .address(restaurant.getAddress())
                .longitude(restaurant.getLongitude())
                .latitude(restaurant.getLatitude())
                .build();
    }
}
