package com.zerobase.shopreservation.manager.service;

import com.zerobase.shopreservation.common.dto.ReviewListInfoDto;
import com.zerobase.shopreservation.common.dto.ReviewOutputDto;
import com.zerobase.shopreservation.common.dto.ReviewOutputPageDto;
import com.zerobase.shopreservation.common.dto.ReviewsOfShopDto;
import com.zerobase.shopreservation.common.entity.Member;
import com.zerobase.shopreservation.common.entity.Reservation;
import com.zerobase.shopreservation.common.entity.Review;
import com.zerobase.shopreservation.common.entity.Shop;
import com.zerobase.shopreservation.common.exception.ShopNotExistException;
import com.zerobase.shopreservation.common.mapper.ReviewMapper;
import com.zerobase.shopreservation.common.repository.MemberRepository;
import com.zerobase.shopreservation.common.type.MemberRole;
import com.zerobase.shopreservation.common.type.ReviewSort;
import com.zerobase.shopreservation.customer.exception.ReviewNotExistException;
import com.zerobase.shopreservation.manager.exception.ShopManagerNotMatchException;
import com.zerobase.shopreservation.manager.repository.ReservationRepository;
import com.zerobase.shopreservation.manager.repository.ReviewRepository;
import com.zerobase.shopreservation.manager.repository.ShopRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Manager ReviewService Test")
class ReviewServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ShopRepository shopRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReviewMapper reviewMapper;

    @InjectMocks
    private ReviewService reviewService;

    private final Member member1 = Member.builder()
            .id(1)
            .email("abc@gmail.com")
            .role(MemberRole.ROLE_MANAGER)
            .password("somehashedvalue")
            .phone("010-3333-2222")
            .build();

    @BeforeEach
    public void setupMember() {
        reviewService.setMemberRepository(memberRepository);

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_MANAGER");
        Collection authorities = Collections.singleton(authority); // Use raw type here

        Authentication authentication = mock(Authentication.class);
        lenient().when(authentication.getAuthorities()).thenReturn(authorities);

        SecurityContext securityContext = mock(SecurityContext.class);
        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        lenient().when(memberRepository.findByEmailAndRole(any(), any()))
                .thenReturn(Optional.of(member1));
    }

    @AfterEach
    public void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("리뷰 목록 보기 - 성공")
    void list_review() {
        //given
        long shopId = 1L;
        Shop shop = Shop.builder()
                .id(shopId)
                .manager(member1)
                .build();
        when(shopRepository.existsByIdAndManager(shopId, member1))
                .thenReturn(true);
        ReviewsOfShopDto dto = ReviewsOfShopDto.builder()
                .shopId(1L)
                .sortBy(ReviewSort.RATING)
                .pageIndex(1)
                .pageSize(10)
                .build();
        when(reviewMapper.selectListInfo(dto))
                .thenReturn(ReviewListInfoDto.builder()
                        .reviewAverage(4.4)
                        .reviewCount(300)
                        .build()
                );
        when(reviewMapper.selectList(dto))
                .thenReturn(List.of(
                        ReviewOutputDto.builder()
                                .id(1L)
                                .rate(3)
                                .contents("nice place")
                                .build(),
                        ReviewOutputDto.builder()
                                .id(2L)
                                .build()
                ));

        //when
        ReviewOutputPageDto page = reviewService.listReviews(dto);

        //then
        assertEquals(page.getReviews().size(), 2);
        assertEquals(page.getReviews().stream().findFirst().get().getRate(), 3);
        assertEquals(page.getReviews().stream().findFirst().get().getContents(), "nice place");
    }

    @Test
    @DisplayName("리뷰 목록 보기 - 실패(shop id가 존재하지 않거나 manager의 shop이 아님)")
    void list_review_fail_no_shop() {
        //given
        when(shopRepository.existsByIdAndManager(anyLong(), any()))
                .thenReturn(false);
        ReviewsOfShopDto dto = ReviewsOfShopDto.builder()
                .shopId(2L)
                .sortBy(ReviewSort.RATING)
                .pageIndex(1)
                .pageSize(10)
                .build();
        //when
        //then
        assertThrows(
                ShopNotExistException.class,
                () -> reviewService.listReviews(dto)
        );
    }

    @Test
    @DisplayName("리뷰 상세 보기 - 성공")
    void detail_review() {
        //given
        long reviewId = 1L;
        Shop shop = Shop.builder()
                .id(1L)
                .manager(member1)
                .build();
        when(reviewRepository.findById(reviewId))
                .thenReturn(Optional.of(
                        Review.builder()
                                .rate(3)
                                .contents("nice place")
                                .shop(shop)
                                .reservation(Reservation.builder().id(1L).build())
                                .build()
                ));

        //when
        ReviewOutputDto detail = reviewService.detailReview(reviewId);

        //then
        assertEquals(detail.getRate(), 3);
    }

    @Test
    @DisplayName("리뷰 상세 보기 - 실패(review id에 맏는 review가 없음")
    void detail_review_fail_no_review() {
        //given
        when(reviewRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        //when
        //then
        assertThrows(ReviewNotExistException.class,
                () -> reviewService.detailReview(1L)
        );
    }

    @Test
    @DisplayName("리뷰 상세 보기 - 실패(review가 로그인한 manager의 shop에 달린 것이 아님)")
    void detail_review_fail_irrelevant_review() {
        //given
        long reviewId = 1L;
        Member member2 = Member.builder()
                .id(2L)
                .build();
        Shop shop = Shop.builder()
                .id(1L)
                .manager(member2)
                .build();
        when(reviewRepository.findById(reviewId))
                .thenReturn(Optional.of(
                        Review.builder()
                                .rate(3)
                                .contents("nice place")
                                .shop(shop)
                                .reservation(Reservation.builder().id(1L).build())
                                .build()
                ));

        //when
        //then
        assertThrows(ShopManagerNotMatchException.class,
                () -> reviewService.detailReview(reviewId)
        );
    }
}