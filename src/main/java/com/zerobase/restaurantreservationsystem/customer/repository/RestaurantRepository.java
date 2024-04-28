package com.zerobase.restaurantreservationsystem.customer.repository;

import com.zerobase.restaurantreservationsystem.common.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("customerRestaurantRepository")
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

}
