package com.zerobase.shopreservation.customer.repository;

import com.zerobase.shopreservation.common.entity.Member;
import com.zerobase.shopreservation.common.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("customerReservationRepository")
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByMemberOrderByScheduleDesc(Member member);
    Optional<Reservation> findByIdAndMember(long id, Member member);
}
