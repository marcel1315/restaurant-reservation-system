package com.zerobase.shopreservation.customer.repository;

import com.zerobase.shopreservation.common.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("customerReviewRepository")
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByReservationId(Long id);

    List<Review> findByShopId(Long id);
}
