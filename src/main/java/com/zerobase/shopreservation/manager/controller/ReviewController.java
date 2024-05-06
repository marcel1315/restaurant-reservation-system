package com.zerobase.shopreservation.manager.controller;

import com.zerobase.shopreservation.common.dto.ReviewOutputDto;
import com.zerobase.shopreservation.common.dto.ReviewOutputPageDto;
import com.zerobase.shopreservation.common.dto.ReviewsOfShopDto;
import com.zerobase.shopreservation.manager.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("managerReviewController")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * 상점의 모든 리뷰를 보기
     * Pagination 되어 있음
     */
    @GetMapping("/manager/reviews")
    public ResponseEntity<?> listReviews(@ModelAttribute ReviewsOfShopDto dto) {
        ReviewOutputPageDto page = reviewService.listReviews(dto);
        return ResponseEntity.ok(page);
    }

    /**
     * 특정 리뷰를 보기
     */
    @GetMapping("/manager/reviews/{reviewId}")
    public ResponseEntity<?> detailReview(@PathVariable long reviewId) {
        ReviewOutputDto detail = reviewService.detailReview(reviewId);
        return ResponseEntity.ok(detail);
    }

    /**
     * 매니저가 자신의 상점의 특정 리뷰를 제거하기
     */
    @DeleteMapping("/manager/reviews/{reviewId}")
    public ResponseEntity<?> removeReview(@PathVariable long reviewId) {
        reviewService.removeReview(reviewId);
        return ResponseEntity.ok(null);
    }
}
