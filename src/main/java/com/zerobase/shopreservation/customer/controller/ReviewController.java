package com.zerobase.shopreservation.customer.controller;

import com.zerobase.shopreservation.common.dto.ReviewOutputDto;
import com.zerobase.shopreservation.customer.dto.ReviewDto;
import com.zerobase.shopreservation.customer.dto.UpdateReviewDto;
import com.zerobase.shopreservation.customer.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

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
    public ResponseEntity<?> listReviews(@RequestParam long shopId) {
        List<ReviewOutputDto> list = reviewService.listReviews(shopId);
        return ResponseEntity.ok(list);
    }
}
