package com.zerobase.restaurantreservationsystem.customer.repository;

import com.zerobase.restaurantreservationsystem.common.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("customerRestaurantRepository")
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    List<Restaurant> findByDeleteMarker(boolean deleteMarker);

    List<Restaurant> findByNameContainsAndDeleteMarker(String name, boolean deleteMarker);

    List<Restaurant> findByAddressContainsAndDeleteMarker(String address, boolean deleteMarker);

    List<Restaurant> findByNameContainsAndAddressContainsAndDeleteMarker(String name, String address, boolean deleteMarker);
}
