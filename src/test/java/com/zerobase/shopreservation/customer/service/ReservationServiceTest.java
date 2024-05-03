package com.zerobase.shopreservation.customer.service;

import com.zerobase.shopreservation.common.dto.ReservationOutputDto;
import com.zerobase.shopreservation.common.entity.Member;
import com.zerobase.shopreservation.common.entity.Reservation;
import com.zerobase.shopreservation.common.entity.Shop;
import com.zerobase.shopreservation.common.exception.ShopNotExistException;
import com.zerobase.shopreservation.common.repository.MemberRepository;
import com.zerobase.shopreservation.common.type.MemberRole;
import com.zerobase.shopreservation.customer.dto.ReservationInputDto;
import com.zerobase.shopreservation.customer.dto.ReservationTimeTableInputDto;
import com.zerobase.shopreservation.customer.dto.ReservationTimeTableOutputDto;
import com.zerobase.shopreservation.customer.exception.InvalidReservationTimeException;
import com.zerobase.shopreservation.customer.repository.ReservationRepository;
import com.zerobase.shopreservation.customer.repository.ShopRepository;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Customer ReservationService Test")
class ReservationServiceTest {

    @Mock
    ReservationRepository reservationRepository;

    @Mock
    ShopRepository shopRepository;

    @Mock
    MemberRepository memberRepository;

    @InjectMocks
    ReservationService reservationService;

    @BeforeEach
    public void setupMember() {
        reservationService.setMemberRepository(memberRepository);

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_CUSTOMER");
        Collection authorities = Collections.singleton(authority); // Use raw type here

        Authentication authentication = mock(Authentication.class);
        lenient().when(authentication.getAuthorities()).thenReturn(authorities);

        SecurityContext securityContext = mock(SecurityContext.class);
        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        Member member1 = Member.builder()
                .id(1)
                .email("abc@gmail.com")
                .role(MemberRole.ROLE_CUSTOMER)
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
    @DisplayName("예약 가능 시간 보기")
    void reservation_timetable() {
        //given
        when(shopRepository.findById(anyLong()))
                .thenReturn(Optional.of(Shop.builder()
                        .reservationStartTimeWeekday(LocalTime.of(13, 00))
                        .reservationFinalTimeWeekday(LocalTime.of(22, 00))
                        .reservationStartTimeWeekend(LocalTime.of(16, 00))
                        .reservationFinalTimeWeekend(LocalTime.of(23, 00))
                        .reservationIntervalMinute(30)
                        .build()
                ));
        ReservationTimeTableInputDto dto1 = ReservationTimeTableInputDto.builder()
                .shopId(1)
                .date(LocalDate.of(2024, 5, 1)) // 2024-05-01은 수요일임
                .build();

        ReservationTimeTableInputDto dto2 = ReservationTimeTableInputDto.builder()
                .shopId(1)
                .date(LocalDate.of(2024, 5, 4)) // 2024-05-04은 토요일임
                .build();

        //when
        ReservationTimeTableOutputDto timeTable1 = reservationService.getReservationTimeTable(dto1);
        ReservationTimeTableOutputDto timeTable2 = reservationService.getReservationTimeTable(dto2);

        //then
        assertEquals(LocalDate.of(2024, 5, 1), timeTable1.getDate());
        assertEquals(LocalTime.of(13, 00), timeTable1.getTimes().stream().findFirst().get());
        assertEquals(19, timeTable1.getTimes().size());

        assertEquals(LocalDate.of(2024, 5, 4), timeTable2.getDate());
        assertEquals(LocalTime.of(16, 00), timeTable2.getTimes().stream().findFirst().get());
        assertEquals(15, timeTable2.getTimes().size());
    }

    @Test
    @DisplayName("예약하기 - 성공")
    void reserve() {
        //given
        given(shopRepository.findById(any()))
                .willReturn(Optional.of(
                        Shop.builder()
                                .id(32)
                                .reservationStartTimeWeekday(LocalTime.of(13, 0))
                                .reservationFinalTimeWeekday(LocalTime.of(22, 0))
                                .reservationIntervalMinute(30)
                                .build()
                ));
        given(reservationRepository.save(any()))
                .willReturn(Reservation.builder()
                        .id(55)
                        .build()
                );
        LocalDateTime schedule = LocalDateTime.of(2024, 5, 2, 15, 0); // 2024-05-02는 목요일
        ReservationInputDto dto = ReservationInputDto.builder()
                .shopId(1)
                .schedule(schedule)
                .phone("010-1111-2222")
                .build();
        //when
        long id = reservationService.reserve(dto);
        ArgumentCaptor<Reservation> captor = ArgumentCaptor.forClass(Reservation.class);

        //then
        verify(reservationRepository, times(1)).save(captor.capture());
        assertEquals(55, id); // id 반환 확인
        assertEquals("abc@gmail.com", captor.getValue().getMember().getEmail()); // 고객 연결 확인
        assertEquals(32, captor.getValue().getShop().getId()); // 상점 연결 확인
    }

    @Test
    @DisplayName("예약하기 - 실패(상점이 존재하지 않음)")
    void reserve_fail_shop_not_exist() {
        //given
        given(shopRepository.findById(any()))
                .willReturn(Optional.empty());
        LocalDateTime oneHourLater = LocalDateTime.now().plusHours(1);
        ReservationInputDto dto = ReservationInputDto.builder()
                .shopId(1)
                .schedule(oneHourLater)
                .phone("010-1111-2222")
                .build();
        //when
        //then
        assertThrows(ShopNotExistException.class,
                () -> reservationService.reserve(dto)
        );
    }

    @Test
    @DisplayName("예약하기 - 실패(요청한 예약 시간이 예약가능 시간표와 맞지 않음)")
    void reserve_fail_invalid_reservation_time() {
        //given
        when(shopRepository.findById(1L))
                .thenReturn(Optional.of(Shop.builder()
                        .id(1)
                        .reservationStartTimeWeekday(LocalTime.of(13, 0))
                        .reservationFinalTimeWeekday(LocalTime.of(22, 0))
                        .reservationIntervalMinute(20)
                        .build()
                )); // 예약가능 시간표 -> 13:20, 13:40, 14:00, 14:20, ..

        LocalDateTime schedule = LocalDateTime.of(2024, 5, 3, 13, 30); // 2024-05-03은 금요일
        ReservationInputDto dto = ReservationInputDto.builder()
                .shopId(1)
                .schedule(schedule)
                .phone("010-1111-2222")
                .build();
        //when
        //then
        assertThrows(InvalidReservationTimeException.class,
                () -> reservationService.reserve(dto)
        );
    }

    @Test
    @DisplayName("예약 목록 확인 - 성공")
    void list_reservations() {
        //given
        given(reservationRepository.findByMemberOrderByScheduleDesc(any()))
                .willReturn(List.of(
                        Reservation.builder()
                                .id(1)
                                .schedule(LocalDateTime.now())
                                .build()
                ));

        //when
        List<ReservationOutputDto> list = reservationService.list();

        //then
        System.out.println("list = " + list);
        assertEquals(1, list.stream().findFirst().get().getId()); // Dto로 잘 변환되어 나가는지 확인
    }
}