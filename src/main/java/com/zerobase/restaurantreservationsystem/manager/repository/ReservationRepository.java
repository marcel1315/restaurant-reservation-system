package com.zerobase.restaurantreservationsystem.manager.repository;

import com.zerobase.restaurantreservationsystem.common.entity.Member;
import com.zerobase.restaurantreservationsystem.common.entity.Reservation;
import com.zerobase.restaurantreservationsystem.common.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByRestaurantId(Long restaurantId);
}
