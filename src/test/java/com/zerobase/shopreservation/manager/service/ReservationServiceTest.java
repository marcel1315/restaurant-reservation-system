package com.zerobase.shopreservation.manager.service;

import com.zerobase.shopreservation.common.dto.ReservationOutputDto;
import com.zerobase.shopreservation.common.dto.ReservationOutputPageDto;
import com.zerobase.shopreservation.common.dto.ReservationsOfShopDto;
import com.zerobase.shopreservation.common.entity.Member;
import com.zerobase.shopreservation.common.entity.Reservation;
import com.zerobase.shopreservation.common.entity.Shop;
import com.zerobase.shopreservation.common.exception.ReservationNotExistException;
import com.zerobase.shopreservation.common.mapper.ReservationMapper;
import com.zerobase.shopreservation.common.repository.MemberRepository;
import com.zerobase.shopreservation.common.type.ApprovalState;
import com.zerobase.shopreservation.common.type.MemberRole;
import com.zerobase.shopreservation.manager.dto.CheckInDto;
import com.zerobase.shopreservation.manager.dto.ReservationApprovalDto;
import com.zerobase.shopreservation.manager.exception.ShopManagerNotMatchException;
import com.zerobase.shopreservation.manager.repository.ReservationRepository;
import com.zerobase.shopreservation.manager.repository.ShopRepository;
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
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Manager ReservationService Test")
class ReservationServiceTest {

    @Mock
    ReservationRepository reservationRepository;

    @Mock
    ShopRepository shopRepository;

    @Mock
    MemberRepository memberRepository;

    @Mock
    ReservationMapper reservationMapper;

    @InjectMocks
    ReservationService reservationService;

    @BeforeEach
    public void setupMember() {
        reservationService.setMemberRepository(memberRepository);

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_MANAGER");
        Collection authorities = Collections.singleton(authority); // Use raw type here

        Authentication authentication = mock(Authentication.class);
        lenient().when(authentication.getAuthorities()).thenReturn(authorities);

        SecurityContext securityContext = mock(SecurityContext.class);
        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Member member1 = Member.builder()
                .id(1)
                .email("abc@gmail.com")
                .role(MemberRole.ROLE_MANAGER)
                .password("somehashedvalue")
                .phone("010-3333-2222")
                .build();
        lenient().when(memberRepository.findByEmailAndRole(any(), any()))
                .thenReturn(Optional.of(member1));
    }

    @AfterEach
    public void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("예약 목록 나열 - 성공")
    void reservation_list() {
        //given
        given(shopRepository.findByManagerAndDeleteMarker(any(), anyBoolean()))
                .willReturn(List.of(
                        Shop.builder()
                                .id(1)
                                .build(),
                        Shop.builder()
                                .id(2)
                                .build()
                ));
        when(reservationMapper.selectListCount(any()))
                .thenReturn(3L);
        when(reservationMapper.selectList(any()))
                .thenReturn(List.of(
                        ReservationOutputDto
                                .builder()
                                .build(),
                        ReservationOutputDto
                                .builder()
                                .build(),
                        ReservationOutputDto
                                .builder()
                                .build()
                ));

        ReservationsOfShopDto dto = ReservationsOfShopDto.builder()
                .shopId(1L)
                .pageIndex(1)
                .pageSize(10)
                .build();
        //when
        ReservationOutputPageDto list = reservationService.list(dto);

        //then
        assertEquals(3, list.getReservations().size());
        assertEquals(3, list.getTotalCount());
    }

    @Test
    @DisplayName("예약 목록 나열 - 실패(상점 manager가 아닌 사람이 그 상점의 예약을 조회함")
    void reservation_list_fail() {
        //given
        given(shopRepository.findByManagerAndDeleteMarker(any(), anyBoolean()))
                .willReturn(List.of());

        ReservationsOfShopDto dto = ReservationsOfShopDto
                .builder()
                .shopId(1L)
                .build();

        //when
        //then
        assertThrows(ShopManagerNotMatchException.class,
                () -> reservationService.list(dto)
        );
    }

    @Test
    @DisplayName("예약 승인 - 성공")
    void reservation_update_approval() {
        //given
        given(shopRepository.findByManagerAndDeleteMarker(any(), anyBoolean()))
                .willReturn(List.of(
                        Shop.builder()
                                .id(1)
                                .build()
                ));
        given(reservationRepository.findById(120L))
                .willReturn(Optional.of(
                                Reservation.builder()
                                        .id(120)
                                        .approvalState(ApprovalState.PENDING)
                                        .build()
                        )
                );

        ReservationApprovalDto dto = ReservationApprovalDto.builder()
                .reservationId(120)
                .shopId(1)
                .approvalState(ApprovalState.ACCEPT)
                .build();

        //when
        reservationService.updateApproval(dto);
        ArgumentCaptor<Reservation> captor = ArgumentCaptor.forClass(Reservation.class);

        //then
        verify(reservationRepository).save(captor.capture());
        assertEquals(ApprovalState.ACCEPT, captor.getValue().getApprovalState());
        assertEquals(120L, captor.getValue().getId());
    }

    @Test
    @DisplayName("예약 승인 - 실패(예약이 없는 경우)")
    void reservation_update_approval_fail_no_reservation() {
        //given
        given(shopRepository.findByManagerAndDeleteMarker(any(), anyBoolean()))
                .willReturn(List.of(
                        Shop.builder()
                                .id(1)
                                .build()
                ));
        given(reservationRepository.findById(120L))
                .willReturn(Optional.empty());

        ReservationApprovalDto dto = ReservationApprovalDto.builder()
                .reservationId(120)
                .shopId(1)
                .approvalState(ApprovalState.ACCEPT)
                .build();

        //when
        //then
        assertThrows(ReservationNotExistException.class,
                () -> reservationService.updateApproval(dto)
        );
    }

    @Test
    @DisplayName("예약 승인 - 실패(manager가 아닌 사람이 조작)")
    void reservation_update_approval_fail_wrong_shop() {
        //given
        given(shopRepository.findByManagerAndDeleteMarker(any(), anyBoolean()))
                .willReturn(List.of(
                        Shop.builder()
                                .id(2)
                                .build()
                ));

        ReservationApprovalDto dto = ReservationApprovalDto.builder()
                .reservationId(120)
                .shopId(1)
                .approvalState(ApprovalState.ACCEPT)
                .build();

        //when
        //then
        assertThrows(ShopManagerNotMatchException.class,
                () -> reservationService.updateApproval(dto)
        );
    }

    @Test
    @DisplayName("체크인 - 성공")
    void checkin() {
        //given
        LocalDateTime now = LocalDateTime.of(2024, 5, 3, 14, 42);
        CheckInDto dto = CheckInDto.builder()
                .reservationId(1)
                .now(now)
                .build();
        LocalDateTime reservedTime = LocalDateTime.of(2024, 5, 3, 15, 0);
        when(reservationRepository.findById(any()))
                .thenReturn(Optional.of(Reservation.builder()
                        .approvalState(ApprovalState.ACCEPT)
                        .checkInAt(null)
                        .schedule(reservedTime)
                        .build()
                ));
        when(reservationRepository.save(any()))
                .thenReturn(Reservation.builder().id(1).build());

        //when
        reservationService.checkIn(dto);
        ArgumentCaptor<Reservation> captor = ArgumentCaptor.forClass(Reservation.class);

        //then
        verify(reservationRepository).save(captor.capture());
        assertEquals(now, captor.getValue().getCheckInAt());
        assertEquals(reservedTime, captor.getValue().getSchedule());
    }
}