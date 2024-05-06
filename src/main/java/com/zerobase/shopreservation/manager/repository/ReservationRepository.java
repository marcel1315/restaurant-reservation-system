package com.zerobase.shopreservation.manager.repository;

import com.zerobase.shopreservation.common.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("managerReservationRepository")
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

}
