package com.zerobase.shopreservation.manager.repository;

import com.zerobase.shopreservation.common.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("managerReviewRepository")
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByShopId(long id);
}
