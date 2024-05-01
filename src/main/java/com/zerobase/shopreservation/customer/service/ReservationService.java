package com.zerobase.shopreservation.customer.service;

import com.zerobase.shopreservation.common.dto.ReservationOutputDto;
import com.zerobase.shopreservation.common.entity.Reservation;
import com.zerobase.shopreservation.common.entity.Shop;
import com.zerobase.shopreservation.common.exception.ReservationNotExistException;
import com.zerobase.shopreservation.common.exception.ShopNotExistException;
import com.zerobase.shopreservation.common.service.BaseService;
import com.zerobase.shopreservation.customer.dto.CheckInDto;
import com.zerobase.shopreservation.customer.dto.ReservationInputDto;
import com.zerobase.shopreservation.customer.exception.CantReservePastTimeException;
import com.zerobase.shopreservation.customer.repository.ReservationRepository;
import com.zerobase.shopreservation.customer.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("customerReservationService")
@RequiredArgsConstructor
public class ReservationService extends BaseService {

    private final ReservationRepository reservationRepository;
    private final ShopRepository shopRepository;

    /**
     * 예약하기
     */
    @Transactional
    public long reserve(ReservationInputDto reservationInputDto) {
        if (reservationInputDto.getSchedule().isBefore(LocalDateTime.now())) {
            throw new CantReservePastTimeException();
        }


        Reservation reservation = reservationInputDto.toReservation();
        reservation.setShop(getShop(reservationInputDto.getShopId()));
        reservation.setMember(getCustomer());

        Reservation saved = reservationRepository.save(reservation);
        return saved.getId();
    }

    /**
     * 예약 목록
     */
    public List<ReservationOutputDto> list() {
        List<Reservation> list = reservationRepository.findByMemberOrderByScheduleDesc(getCustomer());
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
        Optional<Reservation> optionalReservation = reservationRepository.findByIdAndMember(
                checkInDto.getReservationId(),
                getCustomer()
        );

        if (optionalReservation.isEmpty()) {
            throw new ReservationNotExistException();
        }

        optionalReservation.get().setCheckInAt(checkInDto.getCheckInTime());
        Reservation reservation = reservationRepository.save(optionalReservation.get());

        return reservation.getId();
    }
}
