package com.zerobase.restaurantreservationsystem.customer.service;

import com.zerobase.restaurantreservationsystem.common.entity.Restaurant;
import com.zerobase.restaurantreservationsystem.customer.dto.RestaurantDto;
import com.zerobase.restaurantreservationsystem.customer.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("customerRestaurantService")
@RequiredArgsConstructor
public class RestaurantService {

    final private RestaurantRepository restaurantRepository;

    public List<RestaurantDto> list() {
        List<Restaurant> list = restaurantRepository.findAll();
        List<RestaurantDto> listDto = new ArrayList<>();
        for (Restaurant r : list) {
            listDto.add(RestaurantDto.of(r));
        }
        return listDto;
    }
}
