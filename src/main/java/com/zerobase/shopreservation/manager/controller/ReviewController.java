package com.zerobase.shopreservation.manager.controller;

import com.zerobase.shopreservation.common.dto.ReviewOutputDto;
import com.zerobase.shopreservation.common.dto.ReviewOutputPageDto;
import com.zerobase.shopreservation.customer.dto.ReviewsOfShopDto;
import com.zerobase.shopreservation.manager.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("managerReviewController")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/manager/reviews")
    public ResponseEntity<?> listReviews(@ModelAttribute ReviewsOfShopDto dto) {
        ReviewOutputPageDto page = reviewService.listReviews(dto);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/manager/reviews/{reviewId}")
    public ResponseEntity<?> detailReview(@PathVariable long reviewId) {
        ReviewOutputDto detail = reviewService.detailReview(reviewId);
        return ResponseEntity.ok(detail);
    }

    @DeleteMapping("/manager/reviews/{reviewId}")
    public ResponseEntity<?> removeReview(@PathVariable long reviewId) {
        reviewService.removeReview(reviewId);
        return ResponseEntity.ok(null);
    }
}
