package com.zerobase.shopreservation.customer.controller;

import com.zerobase.shopreservation.common.dto.ReviewOutputPageDto;
import com.zerobase.shopreservation.customer.dto.ReviewDto;
import com.zerobase.shopreservation.common.dto.ReviewsOfShopDto;
import com.zerobase.shopreservation.customer.dto.UpdateReviewDto;
import com.zerobase.shopreservation.customer.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController("customerReviewController")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/customer/review")
    public ResponseEntity<?> review(@Validated @RequestBody ReviewDto reviewDto) {
        LocalDateTime now = LocalDateTime.now();
        reviewService.review(reviewDto, now);
        return ResponseEntity.ok(null);
    }

    @PostMapping("/customer/review/{reviewId}")
    public ResponseEntity<?> review(@Validated @RequestBody UpdateReviewDto updateReviewDto, @PathVariable long reviewId) {
        reviewService.updateReview(reviewId, updateReviewDto);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/customer/review/{reviewId}")
    public ResponseEntity<?> review(@PathVariable long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/customer/reviews")
    public ResponseEntity<?> listReviews(@ModelAttribute ReviewsOfShopDto dto) {
        ReviewOutputPageDto page = reviewService.listReviews(dto);
        return ResponseEntity.ok(page);
    }
}
