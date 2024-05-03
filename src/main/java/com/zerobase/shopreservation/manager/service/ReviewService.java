package com.zerobase.shopreservation.manager.service;

import com.zerobase.shopreservation.common.dto.ReviewOutputDto;
import com.zerobase.shopreservation.common.entity.Review;
import com.zerobase.shopreservation.common.exception.ShopNotExistException;
import com.zerobase.shopreservation.common.service.BaseService;
import com.zerobase.shopreservation.customer.exception.ReviewNotExistException;
import com.zerobase.shopreservation.manager.exception.ShopManagerNotMatchException;
import com.zerobase.shopreservation.manager.repository.ReviewRepository;
import com.zerobase.shopreservation.manager.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("managerReviewService")
@RequiredArgsConstructor
public class ReviewService extends BaseService {

    final private ReviewRepository reviewRepository;
    private final ShopRepository shopRepository;

    /**
     * shop의 review 목록 보기
     */
    public List<ReviewOutputDto> listReviews(long shopId) {
        if (!shopRepository.existsByIdAndManager(shopId, getManager())) {
            throw new ShopNotExistException();
        }

        List<Review> reviews = reviewRepository.findByShopId(shopId);
        List<ReviewOutputDto> list = new ArrayList<>();
        for (Review r : reviews) {
            list.add(ReviewOutputDto.of(r));
        }
        return list;
    }

    /**
     * review detail 보기
     */
    public ReviewOutputDto detailReview(long reviewId) {
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);
        if (optionalReview.isEmpty()) {
            throw new ReviewNotExistException();
        }
        Review review = optionalReview.get();

        if (review.getShop().getManager().getId() != getManager().getId()) {
            throw new ShopManagerNotMatchException();
        }

        return ReviewOutputDto.of(review);
    }

    /**
     * review 삭제
     */
    @Transactional
    public void removeReview(long reviewId) {
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);
        if (optionalReview.isEmpty()) {
            throw new ReviewNotExistException();
        }
        Review review = optionalReview.get();

        if (review.getShop().getManager().getId() != getManager().getId()) {
            throw new ShopManagerNotMatchException();
        }
        reviewRepository.delete(review);
    }
}
