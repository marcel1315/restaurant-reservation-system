package com.zerobase.shopreservation.manager.service;

import com.zerobase.shopreservation.common.dto.ReviewOutputDto;
import com.zerobase.shopreservation.common.entity.Review;
import com.zerobase.shopreservation.common.exception.ShopNotExistException;
import com.zerobase.shopreservation.common.service.BaseService;
import com.zerobase.shopreservation.manager.repository.ReviewRepository;
import com.zerobase.shopreservation.manager.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("managerReviewService")
@RequiredArgsConstructor
public class ReviewService extends BaseService {

    final private ReviewRepository reviewRepository;
    private final ShopRepository shopRepository;

    /**
     * shop의 리뷰 보기
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
}
