package com.zerobase.shopreservation.customer.service;

import com.zerobase.shopreservation.common.dto.ReviewListInfoDto;
import com.zerobase.shopreservation.common.dto.ReviewOutputDto;
import com.zerobase.shopreservation.common.dto.ReviewOutputPageDto;
import com.zerobase.shopreservation.common.dto.ReviewsOfShopDto;
import com.zerobase.shopreservation.common.entity.Member;
import com.zerobase.shopreservation.common.entity.Reservation;
import com.zerobase.shopreservation.common.entity.Review;
import com.zerobase.shopreservation.common.entity.Shop;
import com.zerobase.shopreservation.common.exception.ReservationNotExistException;
import com.zerobase.shopreservation.common.repository.MemberRepository;
import com.zerobase.shopreservation.common.type.MemberRole;
import com.zerobase.shopreservation.customer.dto.ReviewDto;
import com.zerobase.shopreservation.customer.dto.UpdateReviewDto;
import com.zerobase.shopreservation.customer.exception.*;
import com.zerobase.shopreservation.customer.mapper.ReviewMapper;
import com.zerobase.shopreservation.customer.repository.ReservationRepository;
import com.zerobase.shopreservation.customer.repository.ReviewRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Customer Review Test")
class ReviewServiceTest {

    @Mock
    MemberRepository memberRepository;

    @Mock
    ReviewRepository reviewRepository;

    @Mock
    ReservationRepository reservationRepository;

    @Mock
    ReviewMapper reviewMapper;

    @InjectMocks
    ReviewService reviewService;

    Member member1 = Member.builder()
            .id(1)
            .email("abc@gmail.com")
            .role(MemberRole.ROLE_CUSTOMER)
            .password("somehashedvalue")
            .phone("010-3333-2222")
            .build();

    @BeforeEach
    public void setupMember() {
        reviewService.setMemberRepository(memberRepository);

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_CUSTOMER");
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
    @DisplayName("리뷰 작성 - 성공")
    void review() {
        //given
        LocalDateTime reviewSubmitTime = LocalDateTime.of(2024, 5, 3, 16, 0);
        LocalDateTime reservationSchedule = LocalDateTime.of(2024, 5, 3, 15, 0);
        LocalDateTime checkIn = LocalDateTime.of(2024, 5, 3, 14, 45);
        ReviewDto dto = ReviewDto.builder()
                .reservationId(1L)
                .rate(4)
                .contents("It was nice place.")
                .build();
        when(reservationRepository.findByIdAndMember(anyLong(), any()))
                .thenReturn(Optional.of(Reservation.builder()
                        .id(1)
                        .schedule(reservationSchedule)
                        .checkInAt(checkIn)
                        .shop(Shop.builder().id(22).build())
                        .build()
                ));

        //when
        reviewService.review(dto, reviewSubmitTime);
        ArgumentCaptor<Review> captor = ArgumentCaptor.forClass(Review.class);

        //then
        verify(reviewRepository).save(captor.capture());
        assertEquals(4, captor.getValue().getRate());
        assertEquals("It was nice place.", captor.getValue().getContents());
        assertEquals(22L, captor.getValue().getShop().getId()); // reservation이 갖고 있던 shop이 review로 잘 연결되는지 확인
    }

    @Test
    @DisplayName("리뷰 작성 - 실패(해당 reservationId의 예약이 존재하지 않음)")
    void review_fail_no_reservation() {
        //given
        LocalDateTime reviewSubmitTime = LocalDateTime.of(2024, 5, 3, 16, 0);
        ReviewDto dto = ReviewDto.builder()
                .reservationId(1L)
                .rate(4)
                .contents("It was nice place.")
                .build();
        when(reservationRepository.findByIdAndMember(anyLong(), any()))
                .thenReturn(Optional.empty());

        //when
        assertThrows(ReservationNotExistException.class,
                () -> reviewService.review(dto, reviewSubmitTime)
        );
    }

    @Test
    @DisplayName("리뷰 작성 - 실패(아직 예약을 checkIn하지 않았음)")
    void review_fail_no_check_in() {
        //given
        LocalDateTime reviewSubmitTime = LocalDateTime.of(2024, 5, 3, 16, 0);
        LocalDateTime reservationSchedule = LocalDateTime.of(2024, 5, 3, 15, 0);
        ReviewDto dto = ReviewDto.builder()
                .reservationId(1L)
                .rate(4)
                .contents("It was nice place.")
                .build();
        when(reservationRepository.findByIdAndMember(anyLong(), any()))
                .thenReturn(Optional.of(Reservation.builder()
                        .id(1)
                        .schedule(reservationSchedule)
                        .checkInAt(null)
                        .build()
                ));

        //when
        assertThrows(DidntCheckInException.class,
                () -> reviewService.review(dto, reviewSubmitTime)
        );
    }

    @Test
    @DisplayName("리뷰 작성 - 실패(checkIn은 했지만 실제 예약 시간이 지나지 않아 사용하지 않은 상태)")
    void review_fail_check_in_but_no_use() {
        //given
        LocalDateTime reviewSubmitTime = LocalDateTime.of(2024, 5, 3, 14, 50);
        LocalDateTime checkIn = LocalDateTime.of(2024, 5, 3, 14, 45);
        LocalDateTime reservationSchedule = LocalDateTime.of(2024, 5, 3, 15, 0);
        ReviewDto dto = ReviewDto.builder()
                .reservationId(1L)
                .rate(4)
                .contents("It was nice place.")
                .build();
        when(reservationRepository.findByIdAndMember(anyLong(), any()))
                .thenReturn(Optional.of(Reservation.builder()
                        .id(1)
                        .schedule(reservationSchedule)
                        .checkInAt(checkIn)
                        .build()
                ));

        //when
        assertThrows(DidntUseReservationYetException.class,
                () -> reviewService.review(dto, reviewSubmitTime)
        );
    }

    @Test
    @DisplayName("리뷰 작성 - 실패(이미 리뷰를 작성했음)")
    void review_fail_already_wrote_review() {
        //given
        LocalDateTime reviewSubmitTime = LocalDateTime.of(2024, 5, 3, 16, 00);
        LocalDateTime checkIn = LocalDateTime.of(2024, 5, 3, 14, 45);
        LocalDateTime reservationSchedule = LocalDateTime.of(2024, 5, 3, 15, 0);
        ReviewDto dto = ReviewDto.builder()
                .reservationId(1L)
                .rate(4)
                .contents("It was nice place.")
                .build();
        when(reservationRepository.findByIdAndMember(anyLong(), any()))
                .thenReturn(Optional.of(Reservation.builder()
                        .id(1)
                        .schedule(reservationSchedule)
                        .checkInAt(checkIn)
                        .build()
                ));
        when(reviewRepository.findByReservationId(anyLong()))
                .thenReturn(Optional.of(Review.builder()
                        .id(1)
                        .build()
                ));
        //when
        assertThrows(AlreadyDidReviewException.class,
                () -> reviewService.review(dto, reviewSubmitTime)
        );
    }

    @Test
    @DisplayName("리뷰 수정 - 성공")
    void update_review() {
        //given
        long reviewId = 1L;
        Review originalReview = Review.builder()
                .id(reviewId)
                .rate(3)
                .contents("it was not bad.")
                .reservation(Reservation.builder()
                        .id(33)
                        .member(member1)
                        .build())
                .build();
        when(reviewRepository.findById(reviewId))
                .thenReturn(Optional.of(originalReview));

        //when
        UpdateReviewDto dto = UpdateReviewDto.builder()
                .rate(4)
                .contents("It was nice place.")
                .build();
        reviewService.updateReview(reviewId, dto);
        ArgumentCaptor<Review> captor = ArgumentCaptor.forClass(Review.class);

        //then
        verify(reviewRepository).save(captor.capture());
        assertEquals(4, captor.getValue().getRate());
        assertEquals("It was nice place.", captor.getValue().getContents());
        assertEquals(33L, captor.getValue().getReservation().getId()); // reservation 연결이 끊어지지 않았는지 확인
    }

    @Test
    @DisplayName("리뷰 수정 - 실패(reviewId의 review가 없음)")
    void update_review_fail_no_review() {
        //given
        long reviewId = 1L;
        when(reviewRepository.findById(reviewId))
                .thenReturn(Optional.empty());
        UpdateReviewDto dto = UpdateReviewDto.builder()
                .rate(4)
                .contents("It was nice place.")
                .build();

        //when
        assertThrows(ReviewNotExistException.class,
                () -> reviewService.updateReview(reviewId, dto)
        );
    }

    @Test
    @DisplayName("리뷰 수정 - 실패(reviewId의 review 작성자가 요청을 보낸 사람과 다름)")
    void update_review_fail_not_writer() {
        //given
        long reviewId = 1L;
        Member member2 = Member.builder()
                .id(2)
                .build();
        Review originalReview = Review.builder()
                .id(reviewId)
                .rate(3)
                .contents("it was not bad.")
                .reservation(Reservation.builder()
                        .id(33)
                        .member(member2) // 로그인되어 있는 사용자는 id로 1을 가지고 있음
                        .build())
                .build();
        when(reviewRepository.findById(reviewId))
                .thenReturn(Optional.of(originalReview));

        UpdateReviewDto dto = UpdateReviewDto.builder()
                .rate(4)
                .contents("It was nice place.")
                .build();

        //when
        assertThrows(ReviewCustomerNotMatchException.class,
                () -> reviewService.updateReview(reviewId, dto)
        );
    }

    @Test
    @DisplayName("리뷰 제거 - 성공")
    void delete_review() {
        //given
        long reviewId = 1L;
        Review review = Review.builder()
                .id(reviewId)
                .rate(3)
                .contents("it was not bad.")
                .reservation(Reservation.builder()
                        .id(33)
                        .member(member1)
                        .build())
                .build();
        when(reviewRepository.findById(reviewId))
                .thenReturn(Optional.of(review));

        //when
        //then
        reviewService.deleteReview(reviewId);
    }

    @Test
    @DisplayName("리뷰 제거 - 실패(reviewId의 review가 없음)")
    void delete_review_fail_no_review() {
        //given
        long reviewId = 1L;
        when(reviewRepository.findById(reviewId))
                .thenReturn(Optional.empty());

        //when
        assertThrows(ReviewNotExistException.class,
                () -> reviewService.deleteReview(reviewId)
        );
    }

    @Test
    @DisplayName("리뷰 제거 - 실패(reviewId의 review 작성자가 요청을 보낸 사람과 다름)")
    void delete_review_fail_not_writer() {
        //given
        long reviewId = 1L;
        Member member2 = Member.builder()
                .id(2)
                .build();
        Review review = Review.builder()
                .id(reviewId)
                .rate(3)
                .contents("it was not bad.")
                .reservation(Reservation.builder()
                        .id(33)
                        .member(member2) // 로그인되어 있는 사용자는 id로 1을 가지고 있음
                        .build())
                .build();
        when(reviewRepository.findById(reviewId))
                .thenReturn(Optional.of(review));

        //when
        assertThrows(ReviewCustomerNotMatchException.class,
                () -> reviewService.deleteReview(reviewId)
        );
    }

    @Test
    @DisplayName("리뷰 목록 보기 - 성공")
    void list_reviews() {
        //given
        long shopId = 1L;
        when(reviewMapper.selectList(any()))
                .thenReturn(List.of(
                        ReviewOutputDto.builder()
                                .id(12L)
                                .build(),
                        ReviewOutputDto.builder()
                                .id(13L)
                                .build()
                ));
        when(reviewMapper.selectListInfo(any()))
                .thenReturn(ReviewListInfoDto.builder()
                        .reviewAverage(3.52)
                        .reviewCount(5)
                        .build());
        ReviewsOfShopDto dto = ReviewsOfShopDto.builder()
                .shopId(shopId)
                .pageIndex(1)
                .pageSize(10)
                .build();

        //when
        ReviewOutputPageDto page = reviewService.listReviews(dto);

        //then
        assertEquals(12L, page.getReviews().stream().findFirst().get().getId());
    }
}