package com.zerobase.shopreservation.manager.repository;

import com.zerobase.shopreservation.common.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByShopId(Long shopId);
}
