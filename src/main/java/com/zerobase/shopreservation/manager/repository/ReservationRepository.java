package com.zerobase.shopreservation.manager.repository;

import com.zerobase.shopreservation.common.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("managerReservationRepository")
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByShopId(Long shopId);
}
