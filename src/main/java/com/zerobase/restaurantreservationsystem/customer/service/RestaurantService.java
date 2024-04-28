package com.zerobase.restaurantreservationsystem.customer.service;

import com.zerobase.restaurantreservationsystem.common.entity.Restaurant;
import com.zerobase.restaurantreservationsystem.common.exception.RestaurantNotExistException;
import com.zerobase.restaurantreservationsystem.customer.dto.RestaurantDto;
import com.zerobase.restaurantreservationsystem.customer.dto.RestaurantSearchDto;
import com.zerobase.restaurantreservationsystem.customer.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("customerRestaurantService")
@RequiredArgsConstructor
public class RestaurantService {

    final private RestaurantRepository restaurantRepository;

    public List<RestaurantDto> list() {
        List<Restaurant> list = restaurantRepository.findByDeleteMarker(false);
        List<RestaurantDto> listDto = new ArrayList<>();
        for (Restaurant r : list) {
            listDto.add(RestaurantDto.of(r));
        }
        return listDto;
    }

    public List<RestaurantDto> search(RestaurantSearchDto restaurantSearchDto) {
        List<Restaurant> list;
        String name = restaurantSearchDto.getName();
        String address = restaurantSearchDto.getAddress();

        if (name != null && address != null) {
            list = restaurantRepository.findByNameContainsAndAddressContainsAndDeleteMarker(name, address, false);
        } else if (name != null) {
            list = restaurantRepository.findByNameContainsAndDeleteMarker(name, false);
        } else if (address != null) {
            list = restaurantRepository.findByAddressContainsAndDeleteMarker(address, false);
        } else {
            list = restaurantRepository.findByDeleteMarker(false);
        }

        List<RestaurantDto> listDto = new ArrayList<>();
        for (Restaurant r : list) {
            listDto.add(RestaurantDto.of(r));
        }
        return listDto;
    }

    public RestaurantDto detail(long id) {
        Optional<Restaurant> restaurant = restaurantRepository.findById(id);
        if (restaurant.isEmpty()) {
            throw new RestaurantNotExistException();
        }
        return RestaurantDto.of(restaurant.get());
    }
}
