package com.zerobase.shopreservation.manager.service;

import com.zerobase.shopreservation.common.dto.ReservationOutputDto;
import com.zerobase.shopreservation.common.entity.Member;
import com.zerobase.shopreservation.common.entity.Reservation;
import com.zerobase.shopreservation.common.entity.Shop;
import com.zerobase.shopreservation.common.exception.ReservationNotExistException;
import com.zerobase.shopreservation.common.service.BaseService;
import com.zerobase.shopreservation.manager.dto.ReservationApprovalDto;
import com.zerobase.shopreservation.manager.exception.ShopManagerNotMatchException;
import com.zerobase.shopreservation.manager.repository.ReservationRepository;
import com.zerobase.shopreservation.manager.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service("managerReservationService")
@RequiredArgsConstructor
public class ReservationService extends BaseService {

    private final ReservationRepository reservationRepository;
    private final ShopRepository shopRepository;

    /**
     * 예약 목록 나열
     * manager가 자신의 shop에 있는 예약을 조회함
     */
    public List<ReservationOutputDto> list(long shopId) {
        checkShopManagerMatch(shopId);

        List<Reservation> reservations = reservationRepository.findAllByShopId(shopId);
        List<ReservationOutputDto> reservationOutputDtos = new ArrayList<>();
        for (Reservation r : reservations) {
            reservationOutputDtos.add(ReservationOutputDto.of(r));
        }
        return reservationOutputDtos;
    }

    /**
     * 상점 예약을 승인/거절함
     * manager가 자신의 shop에 대한 예약에만 승인/거절을 할 수 있음
     */
    public void updateApproval(ReservationApprovalDto approval) {
        checkShopManagerMatch(approval.getShopId());

        Optional<Reservation> optionalReservation = reservationRepository.findById(approval.getReservationId());
        if (optionalReservation.isEmpty()) {
            throw new ReservationNotExistException();
        }

        Reservation reservation = optionalReservation.get();
        reservation.setApprovalState(approval.getApprovalState());
        reservationRepository.save(reservation);
    }

    /**
     * 로그인된 manager가 소유한 shop인지 확인
     * 아닌 경우, ShopManagerNotMatchException
     */
    private void checkShopManagerMatch(long shopId) {
        Member manager = getManager();
        List<Shop> shops = shopRepository.findByManagerAndDeleteMarker(manager, false);
        boolean found = false;
        for (Shop shop : shops) {
            if (shop.getId() == shopId) {
                found = true;
                break;
            }
        }
        if (!found) {
            throw new ShopManagerNotMatchException();
        }
    }
}