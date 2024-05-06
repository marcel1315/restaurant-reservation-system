package com.zerobase.shopreservation.customer.service;

import com.zerobase.shopreservation.common.dto.ReservationOutputDto;
import com.zerobase.shopreservation.common.entity.Reservation;
import com.zerobase.shopreservation.common.entity.Shop;
import com.zerobase.shopreservation.common.exception.ReservationNotExistException;
import com.zerobase.shopreservation.common.exception.ShopNotExistException;
import com.zerobase.shopreservation.common.service.BaseService;
import com.zerobase.shopreservation.common.type.ApprovalState;
import com.zerobase.shopreservation.customer.dto.CheckInDto;
import com.zerobase.shopreservation.customer.dto.ReservationInputDto;
import com.zerobase.shopreservation.customer.dto.ReservationTimeTableInputDto;
import com.zerobase.shopreservation.customer.dto.ReservationTimeTableOutputDto;
import com.zerobase.shopreservation.customer.exception.InvalidReservationTimeException;
import com.zerobase.shopreservation.customer.exception.InvalidTimeTableException;
import com.zerobase.shopreservation.customer.exception.PassedCheckInTimeException;
import com.zerobase.shopreservation.customer.exception.ReservationNotAcceptedException;
import com.zerobase.shopreservation.customer.repository.ReservationRepository;
import com.zerobase.shopreservation.customer.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("customerReservationService")
@RequiredArgsConstructor
public class ReservationService extends BaseService {

    private final ReservationRepository reservationRepository;
    private final ShopRepository shopRepository;

    private static final int CHECKIN_EARLY_MINUTE = 10;

    /**
     * 예약하기
     */
    @Transactional
    public long reserve(ReservationInputDto dto) {
        // 예약 가능한 시간인지 확인
        Shop shop = getShop(dto.shopId);
        LocalTime[] reservationRange = getReservationRange(shop, dto.getSchedule().toLocalDate());

        List<LocalTime> possibleTimes = makeTimetable(
                reservationRange[0],
                reservationRange[1],
                shop.getReservationIntervalMinute()
        );
        if (!possibleTimes.contains(dto.getSchedule().toLocalTime())) {
            throw new InvalidReservationTimeException();
        }

        Reservation reservation = dto.toReservation();
        reservation.setShop(getShop(dto.getShopId()));
        reservation.setMember(getCustomer());

        Reservation saved = reservationRepository.save(reservation);
        return saved.getId();
    }

    /**
     * 예약 가능한 시간 보기
     */
    public ReservationTimeTableOutputDto getReservationTimeTable(ReservationTimeTableInputDto dto) {
        // shop에서 가능한 시간 체크
        Shop shop = getShop(dto.getShopId());
        LocalTime[] reservationRange = getReservationRange(shop, dto.getDate());

        List<LocalTime> times = makeTimetable(
                reservationRange[0],
                reservationRange[1],
                shop.getReservationIntervalMinute()
        );

        return ReservationTimeTableOutputDto.builder()
                .shopId(dto.getShopId())
                .times(times)
                .date(dto.getDate())
                .build();
    }

    /**
     * shop의 주말여부에 따른 예약 범위를 구함
     *
     * @return {startTime, finalTime}
     */
    private LocalTime[] getReservationRange(Shop shop, LocalDate date) {
        LocalTime startTime;
        LocalTime finalTime;
        int day = date.getDayOfWeek().getValue();

        if (day < 6) { // 1~5 Mon~Fri
            startTime = shop.getReservationStartTimeWeekday();
            finalTime = shop.getReservationFinalTimeWeekday();
        } else {
            startTime = shop.getReservationStartTimeWeekend();
            finalTime = shop.getReservationFinalTimeWeekend();
        }

        return new LocalTime[]{startTime, finalTime};
    }

    /**
     * 예약 시간 목록을 생성함
     */
    private List<LocalTime> makeTimetable(LocalTime startTime, LocalTime finalTime, int intervalMinute) {
        if (startTime.isAfter(finalTime)) {
            throw new InvalidTimeTableException();
        }

        List<LocalTime> times = new ArrayList<>();
        for (LocalTime t = startTime; !t.isAfter(finalTime); t = t.plusMinutes(intervalMinute)) {
            times.add(t);
        }
        return times;
    }

    /**
     * 예약한 목록 보기
     */
    public List<ReservationOutputDto> list() {
        LocalDateTime after = LocalDateTime.now().minusDays(1);
        List<Reservation> list = reservationRepository.findByMemberAndScheduleAfterOrderByScheduleDesc(getCustomer(), after);
        List<ReservationOutputDto> listDto = new ArrayList<>();
        for (Reservation r : list) {
            listDto.add(ReservationOutputDto.of(r));
        }
        return listDto;
    }

    /**
     * shop이 존재하는지 체크
     */
    private Shop getShop(long id) {
        Optional<Shop> shop = shopRepository.findById(id);
        if (shop.isEmpty()) {
            throw new ShopNotExistException();
        }
        return shop.get();
    }

    /**
     * 체크인하기
     */
    @Transactional
    public long checkIn(CheckInDto checkInDto) {
        // 예약이 존재하는지 확인
        Optional<Reservation> optionalReservation = reservationRepository.findByIdAndMember(
                checkInDto.getReservationId(),
                getCustomer()
        );
        if (optionalReservation.isEmpty()) {
            throw new ReservationNotExistException();
        }
        Reservation reservation = optionalReservation.get();

        // 체크인 할 수 있는 상태인지 확인 (= 예약시간 10분전인지 확인. 10분전보다 일찍와야 가능한 시나리오)
        if (checkInDto.getNow().isAfter(reservation.getSchedule().minusMinutes(CHECKIN_EARLY_MINUTE))) {
            throw new PassedCheckInTimeException();
        }

        // 예약이 ACCEPT 되었는지 확인
        if (!reservation.getApprovalState().equals(ApprovalState.ACCEPT)) {
            throw new ReservationNotAcceptedException(reservation.getApprovalState());
        }

        // 체크인하고, 저장
        reservation.setCheckInAt(checkInDto.getNow());
        Reservation saved = reservationRepository.save(reservation);

        return saved.getId();
    }
}
