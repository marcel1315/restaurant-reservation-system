package com.zerobase.shopreservation.customer.repository;

import com.zerobase.shopreservation.common.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByReservationId(Long aLong);
}
