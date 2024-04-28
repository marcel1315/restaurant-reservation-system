package com.zerobase.restaurantreservationsystem.manager.repository;

import com.zerobase.restaurantreservationsystem.common.entity.Member;
import com.zerobase.restaurantreservationsystem.common.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    List<Restaurant> findByManagerAndDeleteMarker(Member manager, boolean deleteMarker);

    Optional<Restaurant> findByIdAndManager(long restaurantId, Member manager);


}
